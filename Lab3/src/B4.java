package Lab3.src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class B4 {
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Cách dùng: Java BinaryFileCopy <nguồn> <đích>");
            return;
        }

        Path source = Path.of(args[0]);
        Path target = Path.of(args[1]);
        long totalBytes = 0;

        try (InputStream input = new BufferedInputStream(Files.newInputStream(source));
                OutputStream output = new BufferedOutputStream(Files.newOutputStream(target))) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            // Tự động kiểm tra dung lượng
            long sourceSize = Files.size(source);
            long targetSize = Files.size(target);
            System.out.println("Dung lượng file gốc: " + sourceSize + " byte.");
            System.out.println("Dung lượng file copy: " + targetSize + " byte.");
            if (sourceSize == targetSize) {
                System.out.println("Kiểm thử ĐẠT: Hai file có kích thước bằng nhau.");
            } else {
                System.out.println("Kiểm thử THẤT BẠI: Kích thước chênh lệch.");
            }
            System.out.println("-------------------------------------------------");

            System.out.println("Đã sao chép " + totalBytes + " byte.");
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("Sao chép thất bại: " + e.getMessage());
        }
    }
}
