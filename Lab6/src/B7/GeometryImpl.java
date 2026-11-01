package Lab6.src.B7;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GeometryImpl extends UnicastRemoteObject implements GeometryInterface {

    // Constructor bắt buộc ném ra RemoteException
    public GeometryImpl() throws RemoteException {
        super();
    }

    @Override
    public double rectangleArea(double width, double height) throws RemoteException {
        System.out.println("[Log] Tính diện tích hình chữ nhật: " + width + " x " + height);
        return width * height;
    }

    @Override
    public double circleArea(double radius) throws RemoteException {
        System.out.println("[Log] Tính diện tích hình tròn, bán kính: " + radius);
        return Math.PI * radius * radius;
    }

    @Override
    public double triangleArea(double base, double height) throws RemoteException {
        System.out.println("[Log] Tính diện tích hình tam giác: đáy " + base + ", cao " + height);
        return 0.5 * base * height;
    }
}