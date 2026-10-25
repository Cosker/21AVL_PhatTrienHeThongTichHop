package Lab6.src.B9;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionInterface extends Remote {
    void registerItem(String itemName, double startingPrice) throws RemoteException;

    String getItems() throws RemoteException;

    boolean placeBid(String itemName, double bidAmount, String bidderName) throws RemoteException;

    // Đăng ký Client vào danh sách nhận thông báo (Callback)
    void subscribe(AuctionCallback client) throws RemoteException;
}
