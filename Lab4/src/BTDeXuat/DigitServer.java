package Lab4.src.BTDeXuat;

import java.io.*;
import java.net.*;

public class DigitServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server đang lắng nghe tại cổng 5000...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),
                                true)) {

                    System.out.println("Client đã kết nối.");
                    String input;

                    // Đọc từng dòng thông điệp từ client
                    while ((input = in.readLine()) != null) {
                        if ("QUIT".equals(input)) {
                            System.out.println("Client yêu cầu ngắt kết nối.");
                            break;
                        }
                        out.println(processInput(input));
                    }
                } catch (IOException e) {
                    System.err.println("Lỗi kết nối với Client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processInput(String input) {
        // Kiểm tra nếu chuỗi rỗng, độ dài khác 1, hoặc không phải chữ số
        if (input == null || input.length() != 1 || !Character.isDigit(input.charAt(0))) {
            return "ERR INVALID_DIGIT";
        }

        switch (input.charAt(0)) {
            case '0':
                return "Không";
            case '1':
                return "Một";
            case '2':
                return "Hai";
            case '3':
                return "Ba";
            case '4':
                return "Bốn";
            case '5':
                return "Năm";
            case '6':
                return "Sáu";
            case '7':
                return "Bảy";
            case '8':
                return "Tám";
            case '9':
                return "Chín";
            default:
                return "ERR INVALID_DIGIT";
        }
    }
}