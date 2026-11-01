package Lab6.src.B1;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HelloServer {
    public static void main(String[] args) {
        try {
            // 1. Mở tổng đài RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // 2. Khởi tạo đối tượng chứa logic thực tế
            HelloImpl helloImpl = new HelloImpl();

            // 3. Đăng ký đối tượng đó với tên "HelloService" để Client tìm kiếm
            Naming.rebind("rmi://localhost:1099/HelloService", helloImpl);

            System.out.println("Hello Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
