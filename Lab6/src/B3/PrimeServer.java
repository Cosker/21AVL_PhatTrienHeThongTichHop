package Lab6.src.B3;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class PrimeServer extends UnicastRemoteObject implements PrimeInterface {

    protected PrimeServer() throws RemoteException {
        super();
    }

    @Override
    public boolean isPrime(int n) throws RemoteException {
        System.out.println("Nhận yêu cầu kiểm tra số nguyên tố: " + n);

        // Logic kiểm tra số nguyên tố
        if (n < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            // Mở RMI Registry ở port mặc định 1099
            LocateRegistry.createRegistry(1099);

            // Khởi tạo và đăng ký dịch vụ
            PrimeServer server = new PrimeServer();
            Naming.rebind("rmi://localhost:1099/PrimeService", server);

            System.out.println("Prime Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
