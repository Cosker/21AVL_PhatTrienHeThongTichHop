package Lab6.src.B2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AddImpl extends UnicastRemoteObject implements AddInterface {

    // Constructor bắt buộc phải ném ra RemoteException
    public AddImpl() throws RemoteException {
        super();
    }

    // Nơi chứa logic cốt lõi
    @Override
    public int add(int a, int b) throws RemoteException {
        // In log trên màn hình Server để theo dõi
        System.out.println("[Log] Nhận yêu cầu tính tổng: " + a + " + " + b);

        // Trả về kết quả
        return a + b;
    }
}
