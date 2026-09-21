package Lab4.src.BTDeXuat.udp;

import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UdpTimeServer {
    public static void main(String[] args) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MM yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH mm ss");
        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");

        try (DatagramSocket socket = new DatagramSocket(6001)) {
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String request = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8").trim()
                        .toUpperCase();
                LocalDateTime now = LocalDateTime.now();
                String response = "ERR INVALID_COMMAND";

                switch (request) {
                    case "DATE":
                        response = now.format(dateFmt);
                        break;
                    case "TIME":
                        response = now.format(timeFmt);
                        break;
                    case "DATETIME":
                        response = now.format(dateTimeFmt);
                        break;
                    // Bỏ qua QUIT vì UDP phi kết nối
                }

                byte[] sendData = response.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length,
                        receivePacket.getAddress(), receivePacket.getPort());
                socket.send(sendPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}