package Lab4.src.BTDeXuat.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int PORT = 8000;
    // Cấu trúc thread-safe để lưu trữ danh sách client (Nickname -> PrintWriter)
    private static final ConcurrentHashMap<String, PrintWriter> clients = new ConcurrentHashMap<>();
    // Thread pool để quản lý các luồng linh hoạt
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("Chat Server đang chạy tại cổng " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                // Giao việc xử lý client mới cho Thread Pool
                threadPool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private String nickname;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

                // 1. Xử lý đăng ký Nickname duy nhất
                while (true) {
                    out.println("Vui lòng nhập nickname của bạn:");
                    nickname = in.readLine();
                    if (nickname == null)
                        return; // Client ngắt kết nối trước khi nhập

                    nickname = nickname.trim();
                    if (!nickname.isEmpty() && !clients.containsKey(nickname)) {
                        clients.put(nickname, out);
                        out.println("Đăng ký thành công! Các lệnh hỗ trợ: USERS, MSG <nội_dung>, QUIT");
                        broadcast("[" + nickname + "] đã tham gia phòng chat.");
                        System.out.println("Client tham gia: " + nickname);
                        break;
                    } else {
                        out.println("Nickname đã tồn tại hoặc rỗng. Vui lòng chọn tên khác.");
                    }
                }

                // 2. Xử lý các lệnh từ Client
                String message;
                while ((message = in.readLine()) != null) {
                    String trimmedMsg = message.trim();

                    if (trimmedMsg.equalsIgnoreCase("QUIT")) {
                        break;
                    } else if (trimmedMsg.equalsIgnoreCase("USERS")) {
                        out.println("Danh sách người dùng: " + String.join(", ", clients.keySet()));
                    } else if (trimmedMsg.toUpperCase().startsWith("MSG ")) {
                        String content = trimmedMsg.substring(4);
                        broadcast("[" + nickname + "]: " + content);
                    } else {
                        out.println("Lệnh không hợp lệ. Hãy dùng: USERS, MSG nội_dung, hoặc QUIT");
                    }
                }
            } catch (IOException e) {
                // Xử lý khi Client ngắt kết nối bất thường (VD: đóng terminal cái rụp)
                System.err.println("Cảnh báo: Client [" + nickname + "] ngắt kết nối đột ngột.");
            } finally {
                // Loại client khỏi danh sách và thông báo cho người khác
                if (nickname != null && clients.containsKey(nickname)) {
                    clients.remove(nickname);
                    broadcast("[" + nickname + "] đã rời phòng chat.");
                    System.out.println("Đã dọn dẹp kết nối của: " + nickname);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Hàm gửi tin nhắn tới toàn bộ client đang online
    private static void broadcast(String message) {
        for (PrintWriter writer : clients.values()) {
            writer.println(message);
        }
    }
}
