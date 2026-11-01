package Lab6.src.B3;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class PrimeServer {
    public static void main(String[] args) {
        try {
            // 1. Khởi tạo RMI Registry ở port mặc định 1099
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa thuật toán logic
            PrimeImpl primeImpl = new PrimeImpl();

            // 3. Đăng ký lên mạng với định danh "PrimeService"
            Naming.rebind("rmi://localhost:1099/PrimeService", primeImpl);

            System.out.println("Prime Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
