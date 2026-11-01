package Lab6.src.B9;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class AuctionServer {
    public static void main(String[] args) {
        try {
            // 1. Khởi tạo tổng đài RMI
            LocateRegistry.createRegistry(1099);

            // 2. Tạo đối tượng xử lý nghiệp vụ đấu giá
            AuctionImpl auctionImpl = new AuctionImpl();

            // 3. Đăng ký lên mạng
            Naming.rebind("rmi://localhost:1099/AuctionService", auctionImpl);

            System.out.println("Auction Server đang chạy...");
            System.out.println("Hệ thống đấu giá trực tuyến đã sẵn sàng.");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}