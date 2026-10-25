package Lab6.src.B7;

import java.rmi.Naming;
import java.util.Scanner;

public class GeometryClient {
    public static void main(String[] args) {
        try {
            GeometryInterface geoService = (GeometryInterface) Naming.lookup("rmi://localhost/GeometryService");
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n--- TÍNH DIỆN TÍCH HÌNH HỌC (RMI) ---");
                System.out.println("1. Diện tích Hình chữ nhật");
                System.out.println("2. Diện tích Hình tròn");
                System.out.println("3. Diện tích Hình tam giác");
                System.out.println("0. Thoát");
                System.out.print("Chọn hình cần tính: ");

                // Kiểm tra đầu vào có phải là số nguyên không
                if (!scanner.hasNextInt()) {
                    System.out.println("Vui lòng nhập một số hợp lệ!");
                    scanner.next(); // Bỏ qua ký tự lỗi
                    choice = -1;
                    continue;
                }

                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Nhập chiều dài: ");
                        double w = scanner.nextDouble();
                        System.out.print("Nhập chiều rộng: ");
                        double h = scanner.nextDouble();
                        if (w > 0 && h > 0) {
                            double rectArea = geoService.rectangleArea(w, h);
                            System.out.println("=> Diện tích Hình chữ nhật: " + rectArea);
                        } else {
                            System.out.println("Kích thước phải lớn hơn 0.");
                        }
                        break;

                    case 2:
                        System.out.print("Nhập bán kính: ");
                        double r = scanner.nextDouble();
                        if (r > 0) {
                            double circArea = geoService.circleArea(r);
                            // Định dạng làm tròn 2 chữ số thập phân cho đẹp
                            System.out.printf("=> Diện tích Hình tròn: %.2f\n", circArea);
                        } else {
                            System.out.println("Bán kính phải lớn hơn 0.");
                        }
                        break;

                    case 3:
                        System.out.print("Nhập chiều dài cạnh đáy: ");
                        double base = scanner.nextDouble();
                        System.out.print("Nhập chiều cao: ");
                        double height = scanner.nextDouble();
                        if (base > 0 && height > 0) {
                            double triArea = geoService.triangleArea(base, height);
                            System.out.println("=> Diện tích Hình tam giác: " + triArea);
                        } else {
                            System.out.println("Kích thước phải lớn hơn 0.");
                        }
                        break;

                    case 0:
                        System.out.println("Đã thoát chương trình.");
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
