package Lab4.src.BTDeXuat.multicast;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class MulticastReceiver {
    private static final String MULTICAST_ADDRESS = "239.255.0.1";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        MulticastSocket socket = null;
        NetworkInterface netIf = null;
        SocketAddress groupAddress = null;

        try {
            // 1. Khởi tạo socket tại port 8888
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            groupAddress = new InetSocketAddress(group, PORT);

            // 2. Tìm NetworkInterface phù hợp (Ưu tiên Loopback để test localhost)
            netIf = getBestNetworkInterface();
            if (netIf == null) {
                System.err.println("Không tìm thấy Network Interface nào hỗ trợ Multicast!");
                return;
            }

            System.out.println("=== MULTICAST RECEIVER ===");
            System.out.println("Sử dụng Interface: " + netIf.getDisplayName() + " (" + netIf.getName() + ")");

            // 3. Gia nhập nhóm Multicast VỚI NetworkInterface cụ thể
            socket.joinGroup(groupAddress, netIf);
            System.out.println("Đã tham gia nhóm " + MULTICAST_ADDRESS + ". Đang chờ tin nhắn...");

            // 4. Vòng lặp nhận tin nhắn
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                String senderAddress = packet.getAddress().getHostAddress();

                System.out.println("[Từ " + senderAddress + "]: " + message);

                if ("QUIT".equalsIgnoreCase(message.trim())) {
                    System.out.println("Nhận được lệnh QUIT. Đang tiến hành ngắt kết nối...");
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 5. BẮT BUỘC Rời nhóm (leaveGroup) trước khi đóng socket
            if (socket != null && netIf != null && groupAddress != null) {
                try {
                    System.out.println("Đang rời khỏi group Multicast...");
                    socket.leaveGroup(groupAddress, netIf);
                } catch (IOException e) {
                    System.err.println("Lỗi khi rời group: " + e.getMessage());
                }
                socket.close();
                System.out.println("Đã đóng Socket an toàn.");
            }
        }
    }

    /**
     * Hàm tiện ích: Quét và trả về card mạng tốt nhất để test
     * (Ưu tiên Loopback localhost để tránh bị Firewall/VPN chặn)
     */
    private static NetworkInterface getBestNetworkInterface() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface fallback = null;

        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isUp() && iface.supportsMulticast()) {
                // Ưu tiên Loopback để test thực hành trên cùng 1 máy tính
                if (iface.isLoopback()) {
                    return iface;
                }
                // Lưu lại card LAN/Wi-Fi đề phòng không có loopback
                if (fallback == null && !iface.isVirtual()) {
                    fallback = iface;
                }
            }
        }
        return fallback;
    }
}
