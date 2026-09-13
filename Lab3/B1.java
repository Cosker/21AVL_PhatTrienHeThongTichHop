package Lab3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class B1 {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

        int count = 0;
        System.out.print("Nhập văn bản (nhập q nếu muốn kết thúc): ");

        try {
            while (true) {
                String line = reader.readLine();
                if (line == null || line.equalsIgnoreCase("q")) {
                    break;
                }
                count++;
                System.out.printf("Dòng %d: %s%n", count, line);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Không thể đọc dữ liệu: " + e.getMessage());
        }

        System.out.println("Tổng số dòng đã nhập: " + count);
    }
}
