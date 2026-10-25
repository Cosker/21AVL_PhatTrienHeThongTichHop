package Lab6.src.B6;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class BankServer extends UnicastRemoteObject implements BankInterface {

    // Số dư ban đầu
    private double balance;

    protected BankServer() throws RemoteException {
        super();
        this.balance = 500000.0; // Khởi tạo số dư mặc định là 500,000 VND
    }

    @Override
    public double getBalance() throws RemoteException {
        System.out.println("[Log] Yêu cầu truy vấn số dư.");
        return balance;
    }

    // Sử dụng synchronized để tránh xung đột khi nhiều Client truy cập cùng lúc
    @Override
    public synchronized void deposit(double amount) throws RemoteException {
        if (amount > 0) {
            balance += amount;
            System.out.println("[Log] Giao dịch nạp tiền: +" + amount + " | Số dư mới: " + balance);
        }
    }

    @Override
    public synchronized boolean withdraw(double amount) throws RemoteException {
        System.out.println("[Log] Yêu cầu rút tiền: " + amount);
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            System.out.println("[Log] Giao dịch rút tiền thành công | Số dư mới: " + balance);
            return true;
        }
        System.out.println("[Log] Giao dịch rút tiền thất bại (Không đủ số dư).");
        return false;
    }

    public static void main(String[] args) {
        try {
            // Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            BankServer server = new BankServer();
            Naming.rebind("rmi://localhost:1099/BankService", server);

            System.out.println("Bank Server đang chạy...");
            System.out.println("Số dư ban đầu của hệ thống: 500,000.0");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
