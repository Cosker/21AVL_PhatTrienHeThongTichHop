package Lab6.src.B6;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class BankServer {
    public static void main(String[] args) {
        try {
            // Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // Khởi tạo đối tượng chứa logic (Implementation)
            BankImpl bankImpl = new BankImpl();

            // Đăng ký đối tượng lên mạng để Client gọi
            Naming.rebind("rmi://localhost:1099/BankService", bankImpl);

            System.out.println("Bank Server đang chạy...");
            System.out.println("Hệ thống đã sẵn sàng nhận kết nối từ Client.");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
