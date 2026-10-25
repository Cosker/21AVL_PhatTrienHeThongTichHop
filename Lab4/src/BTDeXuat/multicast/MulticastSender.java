package Lab4.src.BTDeXuat.multicast;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class MulticastSender {
    private static final String MULTICAST_ADDRESS = "239.255.0.1";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in)) {

            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            System.out.println("=== MULTICAST SENDER ===");
            System.out.println("Đang gửi tới group: " + MULTICAST_ADDRESS + ":" + PORT);
            System.out.println("Nhập tin nhắn để gửi (Gõ 'QUIT' để kết thúc):");

            while (true) {
                System.out.print("> ");
                String message = scanner.nextLine();
                byte[] buffer = message.getBytes();

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
                socket.send(packet);

                if ("QUIT".equalsIgnoreCase(message)) {
                    System.out.println("Đã đóng Sender.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
