package Lab4.src.BTDeXuat.log;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class LogClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int PORT = 9000;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)) {
            System.out.println("Đã kết nối tới Log Server.");

            // 1. Nhập và gửi HELLO clientId
            System.out.print("Nhập clientId (chỉ chứa chữ, số, -, _): ");
            String clientId = scanner.nextLine();
            out.println("HELLO " + clientId);

            // 2. Nhận phản hồi từ Server
            String serverResponse = in.readLine();
            System.out.println("Server phản hồi: " + serverResponse);

            if (serverResponse != null && serverResponse.startsWith("ERROR")) {
                System.out.println("Ngắt kết nối do lỗi từ Server.");
                return;
            }

            // 3. Vòng lặp nhập và gửi nội dung
            System.out.println("=== Bắt đầu gửi tin nhắn (Gõ 'QUIT' để thoát) ===");
            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();

                out.println(message);

                if (message.equals("QUIT")) {
                    System.out.println("Đã đóng kết nối.");
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Không thể kết nối đến server: " + e.getMessage());
        }
    }
}
