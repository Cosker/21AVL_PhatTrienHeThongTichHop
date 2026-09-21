package Lab4.src.BTDeXuat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DigitClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                Scanner scanner = new Scanner(System.in, "UTF-8")) {

            System.out.println("Đã kết nối tới Server! Nhập ký tự (0-9) hoặc gõ 'QUIT' để thoát.");

            while (true) {
                System.out.print("> Nhập dữ liệu: ");
                String input = scanner.nextLine();

                // Gửi nguyên văn chuỗi vừa nhập tới Server
                out.println(input);

                if ("QUIT".equals(input)) {
                    System.out.println("Đang thoát chương trình...");
                    break;
                }

                // Nhận phản hồi từ Server
                String response = in.readLine();
                System.out.println("Server phản hồi: " + response);
            }
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới Server. Đảm bảo Server đang chạy.");
        }
    }
}