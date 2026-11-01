package Lab6.src.B8;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class FlightImpl extends UnicastRemoteObject implements FlightInterface {

    // Danh sách lưu trữ mã chuyến bay và số ghế trống
    private HashMap<String, Integer> flights;

    // Constructor thiết lập dữ liệu ban đầu
    public FlightImpl() throws RemoteException {
        super();
        flights = new HashMap<>();
        // Khởi tạo một số chuyến bay mẫu
        flights.put("VN123", 50); // Vietnam Airlines
        flights.put("VJ456", 120); // Vietjet Air
        flights.put("QH789", 10); // Bamboo Airways
    }

    @Override
    public synchronized boolean bookTicket(String flightNumber, int seats) throws RemoteException {
        System.out.println("[Log] Yêu cầu đặt " + seats + " vé cho chuyến " + flightNumber);
        if (flights.containsKey(flightNumber) && seats > 0) {
            int currentSeats = flights.get(flightNumber);
            if (currentSeats >= seats) {
                flights.put(flightNumber, currentSeats - seats);
                System.out.println("[Log] Đặt vé thành công. Còn lại: " + (currentSeats - seats) + " ghế.");
                return true;
            }
        }
        System.out.println("[Log] Đặt vé thất bại (Hết ghế hoặc mã không tồn tại).");
        return false;
    }

    @Override
    public int availableSeats(String flightNumber) throws RemoteException {
        System.out.println("[Log] Tra cứu ghế trống chuyến " + flightNumber);
        // Trả về -1 nếu không tìm thấy chuyến bay
        return flights.getOrDefault(flightNumber, -1);
    }

    @Override
    public synchronized boolean cancelBooking(String flightNumber, int seats) throws RemoteException {
        System.out.println("[Log] Yêu cầu hủy " + seats + " vé chuyến " + flightNumber);
        if (flights.containsKey(flightNumber) && seats > 0) {
            int currentSeats = flights.get(flightNumber);
            flights.put(flightNumber, currentSeats + seats);
            System.out.println("[Log] Hủy vé thành công. Tổng ghế hiện tại: " + (currentSeats + seats));
            return true;
        }
        System.out.println("[Log] Hủy vé thất bại.");
        return false;
    }
}
