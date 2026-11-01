package Lab6.src.B3;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrimeImpl extends UnicastRemoteObject implements PrimeInterface {

    // Constructor bắt buộc ném ra RemoteException
    public PrimeImpl() throws RemoteException {
        super();
    }

    // Nơi chứa logic cốt lõi
    @Override
    public boolean isPrime(int n) throws RemoteException {
        // In log trên Server để quản trị viên theo dõi
        System.out.println("[Log] Nhận yêu cầu kiểm tra số nguyên tố: " + n);

        // Thuật toán kiểm tra số nguyên tố
        if (n < 2)
            return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
