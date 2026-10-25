package Lab6.src.B1;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class HelloServer extends UnicastRemoteObject implements HelloInterface {

    // Constructor phải ném ra RemoteException
    protected HelloServer() throws RemoteException {
        super();
    }

    // Triển khai phương thức sayHello
    @Override
    public String sayHello() throws RemoteException {
        return "Hello, World!";
    }

    public static void main(String[] args) {
        try {
            // Khởi tạo RMI Registry ở cổng mặc định 1099
            LocateRegistry.createRegistry(1099);

            // Tạo đối tượng server
            HelloServer server = new HelloServer();

            // Đăng ký đối tượng với RMI Registry dưới tên "HelloService"
            Naming.rebind("rmi://localhost:1099/HelloService", server);

            System.out.println("Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
