package Lab6.src.B3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PrimeInterface extends Remote {
    boolean isPrime(int n) throws RemoteException;
}
