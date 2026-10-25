package Lab6.src.B5;

import java.rmi.Naming;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try {
            ChatInterface chatService = (ChatInterface) Naming.lookup("rmi://localhost/ChatService");
            Scanner scanner = new Scanner(System.in);
            String message;

            System.out.println("--- ỨNG DỤNG CHAT RMI ---");
            System.out.println("Gõ 'exit' để thoát.\n");

            while (true) {
                System.out.print("Client (Bạn): ");
                message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Đã ngắt kết nối.");
                    break;
                }

                // Gửi tin nhắn lên Server và đợi phản hồi
                String response = chatService.sendMessage(message);

                // In phản hồi từ Server ra màn hình
                System.out.println("Server: " + response);
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
