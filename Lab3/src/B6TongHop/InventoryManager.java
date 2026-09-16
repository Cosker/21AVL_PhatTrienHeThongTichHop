package Lab3.src.B6TongHop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    private static final Path DATA_DIR = Path.of("..", "..", "data");
    private static final Path CSV_FILE = DATA_DIR.resolve("inventory.csv");
    private static final Path REPORT_FILE = DATA_DIR.resolve("inventory-report.txt");

    public static void main(String[] args) {
        try {
            Files.createDirectories(DATA_DIR);
        } catch (IOException e) {
            System.err.println("Không thể tạo thư mục dữ liệu: " + e.getMessage());
            return;
        }

        // Bước 1 & 2: Nhập dữ liệu và lưu CSV
        inputAndSaveProducts();

        // Bước 3: Đọc lại file CSV
        List<Product> products = readProductsFromCsv();

        // Bước 4: Hiển thị và xuất báo cáo
        if (!products.isEmpty()) {
            displayAndReport(products);
        }
    }

    private static void inputAndSaveProducts() {
        List<Product> inputList = new ArrayList<>();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        System.out.println("--- NHẬP SẢN PHẨM (Nhập 'q' tại Mã để kết thúc) ---");

        while (true) {
            try {
                System.out.print("Mã sản phẩm: ");
                String code = console.readLine();
                if (code == null || code.equalsIgnoreCase("q"))
                    break;

                System.out.print("Tên sản phẩm: ");
                String name = console.readLine();

                System.out.print("Đơn giá: ");
                double price = Double.parseDouble(console.readLine());

                System.out.print("Số lượng: ");
                int quantity = Integer.parseInt(console.readLine());

                Product p = new Product(code, name, price, quantity);
                inputList.add(p);
                System.out.println("-> Thêm hợp lệ!\n");

            } catch (NumberFormatException e) {
                System.err.println("-> Lỗi: Đơn giá và số lượng phải là số. Vui lòng nhập lại!\n");
            } catch (IllegalArgumentException e) {
                System.err.println("-> Lỗi: " + e.getMessage() + ". Từ chối dữ liệu!\n");
            } catch (IOException e) {
                System.err.println("-> Lỗi luồng nhập từ bàn phím.\n");
            }
        }

        // Ghi vào file CSV sử dụng try-with-resources và UTF-8
        try (BufferedWriter writer = Files.newBufferedWriter(CSV_FILE, StandardCharsets.UTF_8)) {
            writer.write("ma,ten,donGia,soLuong");
            writer.newLine();
            for (Product p : inputList) {
                writer.write(p.toCsvRow());
                writer.newLine();
            }
            System.out.println("\n[Đã lưu danh sách vào " + CSV_FILE + "]");
        } catch (IOException e) {
            System.err.println("Lỗi ghi tệp " + CSV_FILE + ": " + e.getMessage());
        }
    }

    private static List<Product> readProductsFromCsv() {
        List<Product> list = new ArrayList<>();

        // Bắt trường hợp tệp không tồn tại
        if (!Files.exists(CSV_FILE)) {
            System.err.println("\nThông báo: Không tìm thấy tệp " + CSV_FILE);
            return list;
        }

        try (BufferedReader reader = Files.newBufferedReader(CSV_FILE, StandardCharsets.UTF_8)) {
            reader.readLine(); // Bỏ qua dòng tiêu đề
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank())
                    continue;

                String[] parts = line.split(",", -1);
                // Xử lý csv thiếu cột
                if (parts.length != 4) {
                    System.err.println(
                            "Lỗi tệp " + CSV_FILE + " tại dòng " + lineNumber + ": Thiếu cột dữ liệu. Đã bỏ qua.");
                    continue;
                }

                try {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    double price = Double.parseDouble(parts[2].trim());
                    int quantity = Integer.parseInt(parts[3].trim());

                    list.add(new Product(code, name, price, quantity));
                } catch (NumberFormatException e) {
                    System.err.println(
                            "Lỗi tệp " + CSV_FILE + " tại dòng " + lineNumber + ": Kiểu số không hợp lệ. Đã bỏ qua.");
                } catch (IllegalArgumentException e) {
                    System.err.println(
                            "Lỗi tệp " + CSV_FILE + " tại dòng " + lineNumber + ": " + e.getMessage() + ". Đã bỏ qua.");
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc tệp CSV: " + e.getMessage());
        }
        return list;
    }

    private static void displayAndReport(List<Product> products) {
        double totalInventoryValue = 0;
        Product highestValueProduct = products.get(0);

        System.out.println("\n--- DỮ LIỆU ĐỌC ĐƯỢC TỪ CSV ---");
        for (Product p : products) {
            System.out.println(p);
            double val = p.getInventoryValue();
            totalInventoryValue += val;

            if (val > highestValueProduct.getInventoryValue()) {
                highestValueProduct = p;
            }
        }

        System.out.printf("\nTổng giá trị tồn kho: %,.0f VND%n", totalInventoryValue);

        // Ghi báo cáo inventory-report.txt
        try (BufferedWriter writer = Files.newBufferedWriter(REPORT_FILE, StandardCharsets.UTF_8)) {
            writer.write("=== BÁO CÁO TỒN KHO ===");
            writer.newLine();
            writer.write("Tổng số sản phẩm hợp lệ: " + products.size());
            writer.newLine();
            writer.write(String.format("Tổng giá trị tồn kho: %,.0f VND", totalInventoryValue));
            writer.newLine();
            writer.write(String.format("Sản phẩm có giá trị lớn nhất: %s (%,.0f VND)",
                    highestValueProduct.getName(), highestValueProduct.getInventoryValue()));

            System.out.println("[Đã ghi báo cáo tổng hợp vào " + REPORT_FILE + "]");
        } catch (IOException e) {
            System.err.println("Không ghi được báo cáo " + REPORT_FILE + ": " + e.getMessage());
        }
    }
}