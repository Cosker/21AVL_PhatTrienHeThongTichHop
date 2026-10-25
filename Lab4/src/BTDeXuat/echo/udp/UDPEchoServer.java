package Lab4.src.BTDeXuat.echo.udp;

import java.net.*;

public class UDPEchoServer {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(9001)) {
            System.out.println("UDP Server đang chạy ở port 9001...");
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                DatagramPacket reply = new DatagramPacket(
                        request.getData(), request.getLength(),
                        request.getAddress(), request.getPort());
                socket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
