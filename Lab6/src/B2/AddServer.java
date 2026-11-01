package Lab6.src.B2;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class AddServer {
    public static void main(String[] args) {
        try {
            // 1. Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa logic
            AddImpl addImpl = new AddImpl();

            // 3. Đăng ký đối tượng lên mạng với tên "AddService"
            Naming.rebind("rmi://localhost:1099/AddService", addImpl);

            System.out.println("Add Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
