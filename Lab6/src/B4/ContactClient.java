package Lab6.src.B4;

import java.rmi.Naming;
import java.util.Scanner;

public class ContactClient {
    public static void main(String[] args) {
        try {
            // Tìm đối tượng quản lý danh bạ từ xa
            ContactInterface contactService = (ContactInterface) Naming.lookup("rmi://localhost/ContactService");
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n--- HỆ THỐNG QUẢN LÝ DANH BẠ ---");
                System.out.println("1. Thêm / Sửa liên hệ");
                System.out.println("2. Tìm kiếm liên hệ");
                System.out.println("3. Xóa liên hệ");
                System.out.println("0. Thoát");
                System.out.print("Chọn chức năng: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Loại bỏ ký tự enter thừa

                switch (choice) {
                    case 1:
                        System.out.print("Nhập tên liên hệ: ");
                        String addName = scanner.nextLine();
                        System.out.print("Nhập số điện thoại: ");
                        String phone = scanner.nextLine();
                        contactService.addContact(addName, phone);
                        System.out.println("=> Đã lưu liên hệ thành công!");
                        break;

                    case 2:
                        System.out.print("Nhập tên liên hệ cần tìm: ");
                        String findName = scanner.nextLine();
                        String foundPhone = contactService.findContact(findName);
                        if (foundPhone != null) {
                            System.out.println("=> Số điện thoại của " + findName + " là: " + foundPhone);
                        } else {
                            System.out.println("=> Không tìm thấy liên hệ nào tên '" + findName + "'.");
                        }
                        break;

                    case 3:
                        System.out.print("Nhập tên liên hệ cần xóa: ");
                        String delName = scanner.nextLine();
                        boolean isDeleted = contactService.deleteContact(delName);
                        if (isDeleted) {
                            System.out.println("=> Đã xóa liên hệ '" + delName + "' thành công.");
                        } else {
                            System.out.println("=> Không tìm thấy liên hệ để xóa.");
                        }
                        break;

                    case 0:
                        System.out.println("Đã thoát chương trình.");
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
