package Lab4.src.BTDeXuat.UDPDiscovery_TCPService;

import java.io.*;
import java.net.*;
import java.util.*;

public class DiscoveryClient {
    private static final int UDP_DISCOVERY_PORT = 8888;
    private static final int DISCOVERY_TIMEOUT_MS = 3000; // Đợi 3 giây

    public static void main(String[] args) {
        System.out.println("=== BẮT ĐẦU QUÉT DỊCH VỤ TRONG MẠNG ===");
        List<ServiceInfo> discoveredServices = discoverServices();

        if (discoveredServices.isEmpty()) {
            System.out.println("Không tìm thấy server nào trong mạng.");
            return;
        }

        // Hiển thị danh sách Server
        System.out.println("\n--- DANH SÁCH SERVER TÌM THẤY ---");
        for (int i = 0; i < discoveredServices.size(); i++) {
            ServiceInfo info = discoveredServices.get(i);
            System.out.printf("[%d] %s (IP: %s, TCP Port: %d, Phiên bản: %s)\n",
                    i + 1, info.name, info.ipAddress, info.tcpPort, info.version);
        }

        // Chọn Server để kết nối TCP
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("\nChọn Server muốn kết nối (nhập số): ");
            int choice = scanner.nextInt();

            if (choice > 0 && choice <= discoveredServices.size()) {
                connectToTcpService(discoveredServices.get(choice - 1));
            } else {
                System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // PHASE 1: Gửi UDP Broadcast và Lắng nghe
    private static List<ServiceInfo> discoverServices() {
        // Dùng Set để tự động loại bỏ các phản hồi trùng lặp (dựa vào IP + Port)
        Set<ServiceInfo> uniqueServices = new HashSet<>();

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            socket.setSoTimeout(DISCOVERY_TIMEOUT_MS);

            // Gửi gói DISCOVER_SERVICE
            String request = "DISCOVER_SERVICE";
            byte[] sendData = request.getBytes();
            // Địa chỉ Broadcast cục bộ
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress,
                    UDP_DISCOVERY_PORT);

            socket.send(sendPacket);
            System.out.println("[UDP] Đã gửi gói Broadcast. Đang chờ phản hồi trong " + (DISCOVERY_TIMEOUT_MS / 1000)
                    + " giây...");

            // Lắng nghe phản hồi cho đến khi hết Timeout
            byte[] recvBuf = new byte[1024];
            while (true) {
                try {
                    DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                    socket.receive(receivePacket);

                    String response = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                    String sourceIp = receivePacket.getAddress().getHostAddress();

                    // Xử lý phản hồi: SERVICE tên_dịch_vụ tcp_port phiên_bản
                    if (response.startsWith("SERVICE ")) {
                        String[] parts = response.split(" ");
                        if (parts.length >= 4) {
                            String name = parts[1];
                            int tcpPort = Integer.parseInt(parts[2]);
                            String version = parts[3];

                            ServiceInfo info = new ServiceInfo(name, sourceIp, tcpPort, version);
                            uniqueServices.add(info);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    // Hết 3 giây timeout, thoát vòng lặp
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Chuyển Set sang List để dễ dàng lấy theo Index
        return new ArrayList<>(uniqueServices);
    }

    // PHASE 2: Kết nối TCP dựa trên thông tin lấy từ UDP
    private static void connectToTcpService(ServiceInfo service) {
        System.out.println("\n[TCP] Đang kết nối tới " + service.ipAddress + ":" + service.tcpPort + "...");

        try (Socket socket = new Socket(service.ipAddress, service.tcpPort);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("[TCP] Kết nối thành công! Đang đọc dữ liệu...");
            System.out.println("----------------------------------------");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Server nói: " + line);
            }
            System.out.println("----------------------------------------");
            System.out.println("[TCP] Đã đóng kết nối.");

        } catch (IOException e) {
            System.err.println("[TCP] Lỗi kết nối: " + e.getMessage());
        }
    }

    // Class Data lưu trữ thông tin Service
    // Cài đè equals và hashCode để dùng HashSet loại bỏ trùng lặp
    private static class ServiceInfo {
        String name;
        String ipAddress;
        int tcpPort;
        String version;

        public ServiceInfo(String name, String ipAddress, int tcpPort, String version) {
            this.name = name;
            this.ipAddress = ipAddress;
            this.tcpPort = tcpPort;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            ServiceInfo that = (ServiceInfo) o;
            return tcpPort == that.tcpPort && ipAddress.equals(that.ipAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ipAddress, tcpPort);
        }
    }
}
