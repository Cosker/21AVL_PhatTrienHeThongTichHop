package Lab4.src.BTDeXuat.log;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class LogServer {
    private static final int PORT = 9000;
    private static final String LOG_DIR = "data/logs/";
    // Regex: Chỉ cho phép chữ cái, chữ số, gạch nối, gạch dưới
    private static final Pattern CLIENT_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]+$");

    public static void main(String[] args) {
        // Tạo thư mục lưu trữ nếu chưa tồn tại
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Log Server đang chạy tại port " + PORT + "...");

            // Lắng nghe và chấp nhận nhiều Client cùng lúc
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client mới kết nối: " + clientSocket.getRemoteSocketAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Luồng xử lý cho từng Client
    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                // Đọc tin nhắn đầu tiên (chờ lệnh HELLO)
                String firstMessage = in.readLine();
                if (firstMessage == null || !firstMessage.startsWith("HELLO ")) {
                    out.println("ERROR: Phải bắt đầu bằng lệnh HELLO clientId");
                    return;
                }

                // Tách lấy clientId
                String clientId = firstMessage.substring(6).trim();

                // Kiểm tra tính hợp lệ của clientId
                if (!CLIENT_ID_PATTERN.matcher(clientId).matches()) {
                    out.println("ERROR: clientId không hợp lệ. Chỉ chấp nhận chữ, số, '-' và '_'");
                    return;
                }

                out.println("OK"); // Phản hồi cho Client biết đã chấp nhận

                // Chuẩn bị file log
                File logFile = new File(LOG_DIR + clientId + ".txt");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String remoteAddress = socket.getRemoteSocketAddress().toString();

                // Mở file ở chế độ ghi tiếp (append)
                try (PrintWriter fileWriter = new PrintWriter(new FileWriter(logFile, true))) {
                    String message;
                    // Lắng nghe các tin nhắn tiếp theo
                    while ((message = in.readLine()) != null) {
                        if (message.equals("QUIT")) {
                            System.out.println("Client " + clientId + " đã ngắt kết nối (QUIT).");
                            break;
                        }

                        // Định dạng: [timestamp] - [địa chỉ remote] - [nội dung]
                        String timestamp = LocalDateTime.now().format(timeFormatter);
                        String logEntry = String.format("[%s] - [%s] - %s", timestamp, remoteAddress, message);

                        fileWriter.println(logEntry);
                        fileWriter.flush(); // Đảm bảo ghi ngay xuống đĩa
                    }
                }
            } catch (IOException e) {
                System.err.println("Lỗi xử lý client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
