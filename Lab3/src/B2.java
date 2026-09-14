package Lab3.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class B2 {
    public static void main(String[] args) {
        Path file = Path.of("..", "data", "ghi_chu_B2.txt");

        try {
            Files.createDirectories(file.getParent());

            try (BufferedWriter write = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                write.write("Java I/O làm việc với các luồng dữ liệu.");
                write.newLine();
                write.write("BufferedWrite giúp ghi văn bản hiệu quả.");
                write.newLine();
                write.write("UTP-8 hỗ trợ tiếng Việt ổn định.");
            }

            try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
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
