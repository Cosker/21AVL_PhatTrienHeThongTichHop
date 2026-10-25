package Lab4.src.BTDeXuat.securefile;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.MessageDigest;

public class SecureFileClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 9002;
    private static final String CLIENT_TEMP_DIR = "client_temp/";

    public static void main(String[] args) {
        // Tạo thư mục tạm để sinh file test
        new File(CLIENT_TEMP_DIR).mkdirs();

        System.out.println("=== BẮT ĐẦU KIỂM THỬ TRUYỀN FILE AN TOÀN ===");
        try {
            runTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void runTests() throws Exception {
        // 1. Kiểm thử file rỗng
        File emptyFile = new File(CLIENT_TEMP_DIR + "rong.txt");
        emptyFile.createNewFile(); // 0 bytes
        sendFile(emptyFile, emptyFile.getName());

        // 2. Kiểm thử file văn bản tiếng Việt (UTF-8)
        File viFile = new File(CLIENT_TEMP_DIR + "tiengviet.txt");
        Files.write(viFile.toPath(), "Thử nghiệm gửi file tiếng Việt có dấu. Hệ thống tích hợp.".getBytes("UTF-8"));
        sendFile(viFile, viFile.getName());

        // 3. Kiểm thử file ảnh (Tạo một file nhị phân giả lập header của JPG)
        File imgFile = new File(CLIENT_TEMP_DIR + "anh_test.jpg");
        byte[] fakeJpgData = { (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0, 0x00, 0x10, 0x4A, 0x46, 0x49, 0x46 };
        Files.write(imgFile.toPath(), fakeJpgData);
        sendFile(imgFile, imgFile.getName());

        // 4. Kiểm thử bảo mật Path Traversal (Tên chứa ../)
        File hackFile = new File(CLIENT_TEMP_DIR + "doc.txt");
        Files.write(hackFile.toPath(), "Noi dung bi mat".getBytes());
        // Gửi nội dung file hackFile nhưng cố tình khai báo tên độc hại
        System.out.println("\n[KIỂM THỬ BẢO MẬT] Cố tình gửi tên chứa ../");
        sendFile(hackFile, "../../../windows/system32/fake_hack.txt");
    }

    private static void sendFile(File file, String customFileName) {
        try (Socket socket = new Socket(SERVER_IP, PORT);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                DataInputStream dis = new DataInputStream(socket.getInputStream())) {

            System.out.println("\n[CLIENT] Đang gửi: " + customFileName);

            long fileSize = file.length();
            String fileHash = calculateSHA256(file);

            // 1. Gửi Header
            dos.writeUTF(customFileName); // Tên do client tùy chỉnh
            dos.writeLong(fileSize);
            dos.writeUTF(fileHash);

            // 2. Gửi Payload
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, read);
                }
            }

            // 3. Nhận kết quả
            String response = dis.readUTF();
            System.out.println("   -> Phản hồi từ Server: " + response);

        } catch (Exception e) {
            System.err.println("[CLIENT] Lỗi gửi file: " + e.getMessage());
        }
    }

    private static String calculateSHA256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, n);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
