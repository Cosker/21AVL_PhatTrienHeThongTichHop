package Lab6.src.B6;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class BankImpl extends UnicastRemoteObject implements BankInterface {

    private double balance;

    // Constructor
    public BankImpl() throws RemoteException {
        super();
        this.balance = 500000.0;
    }

    @Override
    public double getBalance() throws RemoteException {
        System.out.println("[Log] Yêu cầu truy vấn số dư.");
        return balance;
    }

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
            System.out.println("[Log] Giao dịch rút thành công | Số dư mới: " + balance);
            return true;
        }
        System.out.println("[Log] Giao dịch rút tiền thất bại.");
        return false;
    }
}
