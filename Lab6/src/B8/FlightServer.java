package Lab6.src.B8;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class FlightServer {
    public static void main(String[] args) {
        try {
            // 1. Mở RMI Registry
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa cơ sở dữ liệu và logic phòng vé
            FlightImpl flightImpl = new FlightImpl();

            // 3. Đăng ký lên mạng
            Naming.rebind("rmi://localhost:1099/FlightService", flightImpl);

            System.out.println("Flight Server đang chạy...");
            System.out.println("Hệ thống đã khởi tạo các chuyến bay: VN123, VJ456, QH789");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}