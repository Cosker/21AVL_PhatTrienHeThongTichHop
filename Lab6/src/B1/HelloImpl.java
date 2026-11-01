package Lab6.src.B1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements HelloInterface {

    // Constructor bắt buộc phải ném ra RemoteException để khởi tạo
    // UnicastRemoteObject
    public HelloImpl() throws RemoteException {
        super();
    }

    // Nơi chứa logic cốt lõi
    @Override
    public String sayHello() throws RemoteException {
        // In log trên Server để biết có Client kết nối
        System.out.println("[Log] Có một Client vừa gọi hàm sayHello()");

        // Trả kết quả về cho Client
        return "Hello, World!";
    }
}
