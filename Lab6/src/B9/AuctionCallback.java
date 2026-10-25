package Lab6.src.B9;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionCallback extends Remote {
    // Phương thức này sẽ được Server gọi để gửi thông báo cho Client
    void notifyUpdate(String message) throws RemoteException;
}
