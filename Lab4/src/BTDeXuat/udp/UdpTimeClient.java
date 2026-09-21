package Lab4.src.BTDeXuat.udp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpTimeClient {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in, "UTF-8")) {

            // Đặt thời gian chờ để minh họa hành vi khi Server sập
            socket.setSoTimeout(3000);
            InetAddress serverAddress = InetAddress.getByName("localhost");

            while (true) {
                System.out.print("Nhập lệnh (DATE, TIME, DATETIME, QUIT): ");
                String cmd = scanner.nextLine();

                if (cmd.equalsIgnoreCase("QUIT")) {
                    break;
                }

                byte[] sendData = cmd.getBytes("UTF-8");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 6001);
                socket.send(sendPacket);

                try {
                    byte[] receiveBuffer = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                    System.out.println("Server: " + response);
                } catch (SocketTimeoutException e) {
                    System.err.println("Lỗi: Đã quá thời gian chờ (Server có thể đang không hoạt động).");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
