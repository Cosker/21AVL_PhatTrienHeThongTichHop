package Lab4.src.BTDeXuat.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                Scanner scanner = new Scanner(System.in, "UTF-8")) {

            // Luồng phụ: Liên tục đọc dữ liệu từ Server và in ra màn hình
            Thread readThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Đã ngắt kết nối từ Server.");
                }
            });
            readThread.start();

            // Luồng chính: Đọc dữ liệu từ bàn phím và gửi lên Server
            while (true) {
                String input = scanner.nextLine();
                out.println(input);

                if (input.trim().equalsIgnoreCase("QUIT")) {
                    System.out.println("Đang thoát...");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới Server: " + e.getMessage());
        }
    }
}
