package Lab6.src.B8;

import java.rmi.Naming;
import java.util.Scanner;

public class FlightClient {
    public static void main(String[] args) {
        try {
            FlightInterface flightService = (FlightInterface) Naming.lookup("rmi://localhost/FlightService");
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n=== HỆ THỐNG ĐẶT VÉ MÁY BAY ===");
                System.out.println("1. Kiểm tra số ghế trống");
                System.out.println("2. Đặt vé");
                System.out.println("3. Hủy vé");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Bỏ qua ký tự enter thừa

                switch (choice) {
                    case 1:
                        System.out.print("Nhập mã chuyến bay (VD: VN123, VJ456): ");
                        String checkFlight = scanner.nextLine();
                        int seats = flightService.availableSeats(checkFlight);
                        if (seats != -1) {
                            System.out.println("=> Chuyến bay " + checkFlight + " hiện có " + seats + " ghế trống.");
                        } else {
                            System.out.println("=> Không tìm thấy chuyến bay này trong hệ thống.");
                        }
                        break;

                    case 2:
                        System.out.print("Nhập mã chuyến bay muốn đặt: ");
                        String bookFlight = scanner.nextLine();
                        System.out.print("Nhập số lượng vé: ");
                        int bookSeats = scanner.nextInt();
                        boolean isBooked = flightService.bookTicket(bookFlight, bookSeats);

                        if (isBooked) {
                            System.out.println("=> THÀNH CÔNG! Đã đặt " + bookSeats + " vé cho chuyến " + bookFlight);
                        } else {
                            System.out.println("=> THẤT BẠI! Chuyến bay không tồn tại hoặc không đủ ghế.");
                        }
                        break;

                    case 3:
                        System.out.print("Nhập mã chuyến bay muốn hủy vé: ");
                        String cancelFlight = scanner.nextLine();
                        System.out.print("Nhập số lượng vé cần hủy (hoàn trả ghế): ");
                        int cancelSeats = scanner.nextInt();
                        boolean isCanceled = flightService.cancelBooking(cancelFlight, cancelSeats);

                        if (isCanceled) {
                            System.out.println("=> THÀNH CÔNG! Đã hủy " + cancelSeats + " vé chuyến " + cancelFlight);
                        } else {
                            System.out.println("=> THẤT BẠI! Mã chuyến bay không đúng.");
                        }
                        break;

                    case 0:
                        System.out.println("Đã thoát ứng dụng đặt vé.");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                }
            } while (choice != 0);

            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
