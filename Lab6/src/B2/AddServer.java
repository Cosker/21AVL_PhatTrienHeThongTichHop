package Lab6.src.B2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class AddServer extends UnicastRemoteObject implements AddInterface {

    protected AddServer() throws RemoteException {
        super();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        System.out.println("Nhận yêu cầu tính tổng: " + a + " + " + b);
        return a + b;
    }

    public static void main(String[] args) {
        try {
            // Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // Khởi tạo server và đăng ký với tên "AddService"
            AddServer server = new AddServer();
            Naming.rebind("rmi://localhost:1099/AddService", server);

            System.out.println("Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
