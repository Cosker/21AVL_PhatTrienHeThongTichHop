package Lab6.src.B5;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class ChatServer extends UnicastRemoteObject implements ChatInterface {

    private transient Scanner scanner;

    protected ChatServer() throws RemoteException {
        super();
        scanner = new Scanner(System.in);
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        // Hiển thị tin nhắn từ Client
        System.out.println("Client: " + message);

        // Nhập tin nhắn phản hồi từ bàn phím của Server
        System.out.print("Server (Bạn): ");
        return scanner.nextLine();
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            ChatServer server = new ChatServer();
            Naming.rebind("rmi://localhost:1099/ChatService", server);

            System.out.println("Chat Server đang chạy...");
            System.out.println("Đang chờ tin nhắn từ Client...\n");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
