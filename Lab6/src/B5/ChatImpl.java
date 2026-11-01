package Lab6.src.B5;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ChatImpl extends UnicastRemoteObject implements ChatInterface {

    // Dùng transient để Scanner không bị tuần tự hóa (Serialization) khi gửi qua
    // mạng
    private transient Scanner scanner;

    // Constructor
    public ChatImpl() throws RemoteException {
        super();
        scanner = new Scanner(System.in);
    }

    @Override
    public String sendMessage(String message) throws RemoteException {
        // 1. Hiển thị tin nhắn do Client gửi đến
        System.out.println("Client: " + message);

        // 2. Chờ người dùng phía Server nhập tin nhắn phản hồi
        System.out.print("Server (Bạn): ");
        String reply = scanner.nextLine();

        // 3. Trả kết quả về cho Client
        return reply;
    }
}
