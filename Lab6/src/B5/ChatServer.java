package Lab6.src.B5;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ChatServer {
    public static void main(String[] args) {
        try {
            // 1. Mở RMI Registry
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng xử lý chat
            ChatImpl chatImpl = new ChatImpl();

            // 3. Đăng ký dịch vụ lên mạng
            Naming.rebind("rmi://localhost:1099/ChatService", chatImpl);

            System.out.println("Chat Server đang chạy...");
            System.out.println("Đang chờ tin nhắn từ Client...\n");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}