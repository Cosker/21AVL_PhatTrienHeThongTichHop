package Lab4.src.BTDeXuat.tcp;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TcpTimeServer {
    public static void main(String[] args) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd MM yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH mm ss");
        DateTimeFormatter dateTimeFmt = DateTimeFormatter.ofPattern("dd MM yyyy HH mm ss");

        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Server đang lắng nghe tại cổng 6000. Đang chờ Client kết nối...");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Có Client mới kết nối từ IP: " + socket.getInetAddress().getHostAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

                    String request;
                    while ((request = in.readLine()) != null) {
                        request = request.trim().toUpperCase();
                        if (request.equals("QUIT")) {
                            System.out.println(
                                    "Client " + socket.getInetAddress().getHostAddress() + " đã ngắt kết nối.");
                            break;
                        }

                        LocalDateTime now = LocalDateTime.now();
                        switch (request) {
                            case "DATE":
                                out.println(now.format(dateFmt));
                                break;
                            case "TIME":
                                out.println(now.format(timeFmt));
                                break;
                            case "DATETIME":
                                out.println(now.format(dateTimeFmt));
                                break;
                            default:
                                out.println("ERR INVALID_COMMAND");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Lỗi kết nối client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
