package Lab4.src.BTDeXuat.echo.tcp;

import java.io.*;
import java.net.*;

public class TCPEchoServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9000)) {
            System.out.println("TCP Server đang chạy ở port 9000...");
            while (true) {
                Socket socket = serverSocket.accept();
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                // Đọc và phản hồi liên tục cho 1 client
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
