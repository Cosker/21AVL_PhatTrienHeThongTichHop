package Lab6.src.B2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddInterface extends Remote {
    int add(int a, int b) throws RemoteException;
}
