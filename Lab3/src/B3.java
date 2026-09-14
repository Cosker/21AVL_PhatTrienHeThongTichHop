package Lab3.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class B3 {
    public static void main(String[] args) {
        Path file = Path.of("..", "data", "ghi_chu_B3.txt");

        try {
            Files.createDirectories(file.getParent());

            // In đường dẫn tuyệt đối của tệp
            System.out.println("Đường dẫn tuyệt đối của tệp: " + file.toAbsolutePath());
            System.out.println();

            // Thay chế độ ghi mới bằng CREATE và APPEND để nối thêm nội dung
            try (BufferedWriter write = Files.newBufferedWriter(file, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                write.write("Java I/O làm việc với các luồng dữ liệu.");
                write.newLine();
                write.write("BufferedWrite giúp ghi văn bản hiệu quả.");
                write.newLine();
                write.write("UTF-8 hỗ trợ tiếng Việt ổn định.");
                write.newLine();

                write.write("--- Dòng này được nối thêm ---");
                write.newLine();
            }

            System.out.println("--- Kết quả đọc bằng UTF-8 ---");
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                String line;
                int number = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.printf("%d. %s%n", number++, line);
                }
            }

            // Đọc cùng tệp bằng charset khác
            System.out.println("\n--- Kết quả đọc bằng ISO_8859_1 ---");
            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.ISO_8859_1)) {
                String line;
                int number = 1;
                while ((line = reader.readLine()) != null) {
                    System.out.printf("%d. %s%n", number++, line);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Lỗi xử lý tệp " + file + ": " + e.getMessage());
        }
    }
}
