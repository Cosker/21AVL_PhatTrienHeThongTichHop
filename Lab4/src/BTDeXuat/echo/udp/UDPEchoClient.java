package Lab4.src.BTDeXuat.echo.udp;

import java.net.*;

public class UDPEchoClient {
    public static void main(String[] args) {
        byte[] message = new byte[1024];
        int totalMessages = 1000;
        int receivedCount = 0;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("127.0.0.1");
            socket.setSoTimeout(2000); // Timeout 2s

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < totalMessages; i++) {
                DatagramPacket request = new DatagramPacket(message, message.length, address, 9001);
                socket.send(request);

                DatagramPacket response = new DatagramPacket(new byte[1024], 1024);
                try {
                    socket.receive(response);
                    receivedCount++;
                } catch (SocketTimeoutException e) {
                    // Mất gói tin, bỏ qua và gửi gói tiếp theo
                }
            }

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println("=== KẾT QUẢ UDP ===");
            System.out.println("Tổng thời gian: " + totalTime + " ms");
            System.out.println("Số phản hồi nhận được: " + receivedCount + "/" + totalMessages);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
