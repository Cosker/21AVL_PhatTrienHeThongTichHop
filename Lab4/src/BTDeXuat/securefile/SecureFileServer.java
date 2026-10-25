package Lab4.src.BTDeXuat.securefile;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.MessageDigest;

public class SecureFileServer {
    private static final int PORT = 9002;
    private static final String UPLOAD_DIR = "upload/";

    public static void main(String[] args) {
        // Tạo thư mục upload cố định
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists())
            dir.mkdirs();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Secure File Server đang chạy ở port " + PORT + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client kết nối: " + clientSocket.getRemoteSocketAddress());
                new Thread(new FileReceiverHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class FileReceiverHandler implements Runnable {
        private Socket socket;

        public FileReceiverHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
                // 1. Đọc metadata từ Client
                String clientPath = dis.readUTF();
                long fileSize = dis.readLong();
                String clientHash = dis.readUTF();

                System.out.println("\n[SERVER] Đang nhận yêu cầu file: " + clientPath);
                System.out.println("   - Kích thước khai báo: " + fileSize + " bytes");
                System.out.println("   - SHA-256 khai báo: " + clientHash);

                // 2. Bảo mật: Bỏ qua đường dẫn client chỉ định, chỉ lấy tên file cuối cùng
                // Ví dụ: "../../../etc/passwd" -> "passwd"
                String safeFileName = Paths.get(clientPath).getFileName().toString();
                if (safeFileName.equals("..") || safeFileName.equals(".")) {
                    safeFileName = "invalid_name_" + System.currentTimeMillis();
                }

                File destFile = new File(UPLOAD_DIR + safeFileName);
                System.out.println("   -> Lưu an toàn tại: " + destFile.getPath());

                // 3. Đọc chính xác số byte đã khai báo (Không dùng EOF làm ranh giới)
                try (FileOutputStream fos = new FileOutputStream(destFile)) {
                    byte[] buffer = new byte[8192];
                    long remaining = fileSize;

                    while (remaining > 0) {
                        // Chỉ đọc tối đa số byte còn lại của file này
                        int bytesToRead = (int) Math.min(buffer.length, remaining);
                        int read = dis.read(buffer, 0, bytesToRead);
                        if (read == -1)
                            break; // Client ngắt kết nối đột ngột

                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }

                    if (remaining > 0) {
                        System.out.println("[SERVER] Lỗi: Client ngắt kết nối trước khi gửi đủ file.");
                        dos.writeUTF("ERR INCOMPLETE_TRANSFER");
                        return;
                    }
                }

                // 4. Tính lại SHA-256 sau khi lưu
                String serverHash = calculateSHA256(destFile);
                System.out.println("   - SHA-256 thực tế: " + serverHash);

                // 5. So sánh và trả kết quả
                if (serverHash.equals(clientHash)) {
                    System.out.println("[SERVER] Khớp Hash. Nhận thành công!");
                    dos.writeUTF("OK");
                } else {
                    System.out.println("[SERVER] CẢNH BÁO: Không khớp Hash!");
                    dos.writeUTF("ERR HASH_MISMATCH");
                }

            } catch (Exception e) {
                System.err.println("Lỗi xử lý client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        // Hàm tiện ích tính SHA-256 cho file
        private String calculateSHA256(File file) throws Exception {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (InputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int n;
                while ((n = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, n);
                }
            }
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
}
