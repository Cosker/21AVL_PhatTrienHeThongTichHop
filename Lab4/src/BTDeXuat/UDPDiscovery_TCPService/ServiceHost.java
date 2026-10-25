package Lab4.src.BTDeXuat.UDPDiscovery_TCPService;

import java.io.*;
import java.net.*;

public class ServiceHost {
    private static final int UDP_DISCOVERY_PORT = 8888;

    // Cấu hình dịch vụ
    private String serviceName;
    private int tcpPort;
    private String version;

    public ServiceHost(String name, int tcpPort, String version) {
        this.serviceName = name;
        this.tcpPort = tcpPort;
        this.version = version;
    }

    public void start() {
        // Khởi chạy TCP Service Thread
        new Thread(this::runTcpServer).start();

        // Khởi chạy UDP Discovery Thread
        new Thread(this::runUdpDiscovery).start();
    }

    // ---------------- UDP DISCOVERY THREAD ----------------
    private void runUdpDiscovery() {
        try (DatagramSocket socket = new DatagramSocket(UDP_DISCOVERY_PORT)) {
            System.out.println("[UDP] Đang lắng nghe yêu cầu DISCOVER tại port " + UDP_DISCOVERY_PORT);
            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String request = new String(packet.getData(), 0, packet.getLength()).trim();

                if ("DISCOVER_SERVICE".equals(request)) {
                    // Định dạng phản hồi: SERVICE tên_dịch_vụ tcp_port phiên_bản
                    String responseStr = String.format("SERVICE %s %d %s", serviceName, tcpPort, version);
                    byte[] responseData = responseStr.getBytes();

                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData, responseData.length,
                            packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);
                    System.out.println("[UDP] Đã trả lời DISCOVER từ: " + packet.getAddress().getHostAddress());
                }
            }
        } catch (Exception e) {
            System.err.println("[UDP] Lỗi Discovery: " + e.getMessage());
        }
    }

    // ---------------- TCP SERVICE THREAD ----------------
    private void runTcpServer() {
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            System.out.println("[TCP] " + serviceName + " đang chạy tại port " + tcpPort + " (v" + version + ")");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[TCP] Client kết nối: " + clientSocket.getRemoteSocketAddress());

                // Trả về thời gian hiện tại và đóng kết nối (Mô phỏng dịch vụ)
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Xin chào! Bạn đang kết nối tới " + serviceName);
                out.println("Thời gian server: " + java.time.LocalDateTime.now());
                out.println("Tạm biệt!");

                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[TCP] Lỗi Service: " + e.getMessage());
        }
    }

    // Khởi chạy một hoặc nhiều instance (Chạy File này nhiều lần với cấu hình khác
    // nhau để test)
    public static void main(String[] args) {
        // Tạo một server giả định
        // Bạn có thể đổi Port và Name rồi chạy thêm 1 terminal nữa để test nhiều server
        ServiceHost host = new ServiceHost("FileServer_ZoneA", 9001, "1.0");
        host.start();
    }
}
