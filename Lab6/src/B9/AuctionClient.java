package Lab6.src.B9;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

// Client cũng implements AuctionCallback để nhận thông báo
public class AuctionClient extends UnicastRemoteObject implements AuctionCallback {

    protected AuctionClient() throws RemoteException {
        super();
    }

    // Hàm này sẽ được Server gọi từ xa mỗi khi có người đấu giá
    @Override
    public void notifyUpdate(String message) throws RemoteException {
        System.out.println("\n\n" + message);
        System.out.print("Chọn chức năng: "); // In lại menu prompt cho người dùng đỡ bỡ ngỡ
    }

    public static void main(String[] args) {
        try {
            AuctionInterface auctionService = (AuctionInterface) Naming.lookup("rmi://localhost/AuctionService");

            // Khởi tạo Client và đăng ký nhận thông báo với Server
            AuctionClient client = new AuctionClient();
            auctionService.subscribe(client);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Vui lòng nhập tên của bạn (Seller/Bidder): ");
            String username = scanner.nextLine();

            int choice;
            do {
                System.out.println("\n=== HỆ THỐNG ĐẤU GIÁ TRỰC TUYẾN ===");
                System.out.println("1. [Seller] Đăng bán sản phẩm mới");
                System.out.println("2. [Bidder] Xem các sản phẩm đang đấu giá");
                System.out.println("3. [Bidder] Ra giá (Bid) cho sản phẩm");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Nhập tên sản phẩm: ");
                        String itemName = scanner.nextLine();
                        System.out.print("Nhập giá khởi điểm: ");
                        double startPrice = scanner.nextDouble();
                        auctionService.registerItem(itemName, startPrice);
                        System.out.println("=> Đã đưa sản phẩm lên sàn thành công!");
                        break;

                    case 2:
                        String list = auctionService.getItems();
                        System.out.println(list);
                        break;

                    case 3:
                        System.out.print("Nhập tên sản phẩm muốn đấu giá: ");
                        String bidItem = scanner.nextLine();
                        System.out.print("Nhập mức giá bạn muốn đưa ra: ");
                        double bidAmount = scanner.nextDouble();

                        boolean success = auctionService.placeBid(bidItem, bidAmount, username);
                        if (success) {
                            System.out.println("=> Bạn đã giữ giá cao nhất!");
                        } else {
                            System.out.println("=> Thất bại! Giá của bạn đưa ra quá thấp hoặc SP không tồn tại.");
                        }
                        break;

                    case 0:
                        System.out.println("Rời khỏi phòng đấu giá.");
                        break;
                }
            } while (choice != 0);

            scanner.close();
            System.exit(0); // Phải gọi lệnh này để ngắt tiến trình nhận thông báo (UnicastRemoteObject)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
