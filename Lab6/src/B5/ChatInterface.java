package Lab6.src.B5;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatInterface extends Remote {
    String sendMessage(String message) throws RemoteException;
}
