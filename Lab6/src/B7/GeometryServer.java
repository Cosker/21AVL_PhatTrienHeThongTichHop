package Lab6.src.B7;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class GeometryServer {
    public static void main(String[] args) {
        try {
            // 1. Mở RMI Registry
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa thuật toán toán học
            GeometryImpl geometryImpl = new GeometryImpl();

            // 3. Đăng ký đối tượng lên mạng với tên "GeometryService"
            Naming.rebind("rmi://localhost:1099/GeometryService", geometryImpl);

            System.out.println("Geometry Server đang chạy và chờ yêu cầu tính toán...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}