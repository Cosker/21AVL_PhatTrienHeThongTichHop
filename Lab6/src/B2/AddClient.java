package Lab6.src.B2;

import java.rmi.Naming;
import java.util.Scanner;

public class AddClient {
    public static void main(String[] args) {
        try {
            // Tìm đối tượng từ xa
            AddInterface addService = (AddInterface) Naming.lookup("rmi://localhost/AddService");

            // Nhập liệu từ bàn phím
            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập số nguyên a: ");
            int a = scanner.nextInt();

            System.out.print("Nhập số nguyên b: ");
            int b = scanner.nextInt();

            // Gọi hàm tính tổng trên Server
            int result = addService.add(a, b);
            System.out.println("Kết quả trả về từ Server: " + a + " + " + b + " = " + result);

            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
