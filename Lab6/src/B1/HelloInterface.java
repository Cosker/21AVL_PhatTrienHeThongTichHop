package Lab6.src.B1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloInterface extends Remote {
    String sayHello() throws RemoteException;
}
