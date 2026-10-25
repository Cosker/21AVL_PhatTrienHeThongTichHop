package Lab4.src.BTDeXuat.echo.tcp;

import java.io.*;
import java.net.*;

public class TCPEchoClient {
    public static void main(String[] args) {
        byte[] message = new byte[1024]; // Thông điệp 1024 bytes
        int totalMessages = 1000;
        int receivedCount = 0;

        try (Socket socket = new Socket("127.0.0.1", 9000)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            byte[] response = new byte[1024];

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < totalMessages; i++) {
                out.write(message);
                int bytesRead = in.read(response);
                if (bytesRead > 0) {
                    receivedCount++;
                }
            }

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println("=== KẾT QUẢ TCP ===");
            System.out.println("Tổng thời gian: " + totalTime + " ms");
            System.out.println("Số phản hồi nhận được: " + receivedCount + "/" + totalMessages);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
