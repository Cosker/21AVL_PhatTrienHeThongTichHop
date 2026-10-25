package Lab6.src.B3;

import java.rmi.Naming;
import java.util.Scanner;

public class PrimeClient {
    public static void main(String[] args) {
        try {
            // Tìm đối tượng từ xa
            PrimeInterface primeService = (PrimeInterface) Naming.lookup("rmi://localhost/PrimeService");

            Scanner scanner = new Scanner(System.in);
            System.out.print("Nhập một số nguyên để kiểm tra: ");

            // Kiểm tra xem người dùng có nhập đúng số nguyên không
            if (scanner.hasNextInt()) {
                int n = scanner.nextInt();

                // Gọi phương thức từ xa
                boolean result = primeService.isPrime(n);

                // Hiển thị kết quả
                if (result) {
                    System.out.println("Kết quả: " + n + " LÀ số nguyên tố.");
                } else {
                    System.out.println("Kết quả: " + n + " KHÔNG PHẢI là số nguyên tố.");
                }
            } else {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
            }

            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
