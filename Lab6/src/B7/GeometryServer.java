package Lab6.src.B7;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class GeometryServer extends UnicastRemoteObject implements GeometryInterface {

    protected GeometryServer() throws RemoteException {
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

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);

            GeometryServer server = new GeometryServer();
            Naming.rebind("rmi://localhost:1099/GeometryService", server);

            System.out.println("Geometry Server đang chạy và chờ yêu cầu tính toán...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
