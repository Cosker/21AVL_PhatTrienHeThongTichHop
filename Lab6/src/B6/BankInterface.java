package Lab6.src.B6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BankInterface extends Remote {
    double getBalance() throws RemoteException;

    void deposit(double amount) throws RemoteException;

    boolean withdraw(double amount) throws RemoteException;
}
