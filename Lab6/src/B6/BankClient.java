package Lab6.src.B6;

import java.rmi.Naming;
import java.util.Scanner;

public class BankClient {
    public static void main(String[] args) {
        try {
            BankInterface bankService = (BankInterface) Naming.lookup("rmi://localhost/BankService");
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n=== HỆ THỐNG NGÂN HÀNG RMI ===");
                System.out.println("1. Kiểm tra số dư");
                System.out.println("2. Nạp tiền");
                System.out.println("3. Rút tiền");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        double balance = bankService.getBalance();
                        System.out.println("=> Số dư hiện tại: " + balance + " VND");
                        break;

                    case 2:
                        System.out.print("Nhập số tiền muốn nạp: ");
                        double depAmount = scanner.nextDouble();
                        if (depAmount > 0) {
                            bankService.deposit(depAmount);
                            System.out.println("=> Giao dịch nạp tiền thành công!");
                        } else {
                            System.out.println("=> Số tiền nạp không hợp lệ.");
                        }
                        break;

                    case 3:
                        System.out.print("Nhập số tiền muốn rút: ");
                        double witAmount = scanner.nextDouble();
                        if (witAmount > 0) {
                            boolean success = bankService.withdraw(witAmount);
                            if (success) {
                                System.out.println("=> Rút tiền thành công! Vui lòng nhận tiền.");
                            } else {
                                System.out.println("=> Giao dịch thất bại! Số dư không đủ.");
                            }
                        } else {
                            System.out.println("=> Số tiền rút không hợp lệ.");
                        }
                        break;

                    case 0:
                        System.out.println("Đã thoát chương trình. Cảm ơn quý khách!");
                        break;

                    default:
                        System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
                }
            } while (choice != 0);

            scanner.close();
        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
