package Lab6.src.B4;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ContactServer {
    public static void main(String[] args) {
        try {
            // 1. Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa cơ sở dữ liệu (HashMap) và logic
            ContactImpl contactImpl = new ContactImpl();

            // 3. Đăng ký lên mạng
            Naming.rebind("rmi://localhost:1099/ContactService", contactImpl);

            System.out.println("Contact Server đang chạy và chờ kết nối...");
            System.out.println("Cơ sở dữ liệu danh bạ (HashMap) đã sẵn sàng.");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}