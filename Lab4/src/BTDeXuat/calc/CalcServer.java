package Lab4.src.BTDeXuat.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(7000)) {
            System.out.println("Server Máy tính đang lắng nghe tại cổng 7000. Chờ Client Máy tính kết nối...");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client kết nối: " + socket.getInetAddress().getHostAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

                    String request;
                    while ((request = in.readLine()) != null) {
                        if (request.trim().equalsIgnoreCase("QUIT")) {
                            System.out.println("Client ngắt kết nối.");
                            break;
                        }
                        out.println(processCalculation(request));
                    }
                } catch (IOException e) {
                    // TODO: handle exceptionredReader in = new BufferedReader(new
                    // InputStreamReader(socket.getInputStream(), "UTF-8"));
                    System.err.println("Lỗi xử lý Client: " + e.getMessage());

                }
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private static String processCalculation(String request) {
        // TODO Auto-generated method stub
        String[] parts = request.trim().split("\\s+");

        // Kiểm tra thiếu thành phần hoặc sai tiền tố
        if (parts.length != 4 || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];
        double num1, num2;

        // Kiểm tra tính hợp lệ của số
        try {
            num1 = Double.parseDouble(parts[2]);
            num2 = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            // TODO: handle exception
            return "ERR INVALID_NUMBER";
        }

        double result = 0;
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    return "ERR DIVIDE_BY_ZERO";
                }
                result = num1 / num2;
                break;
            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }

        // Định dạng kết quả (loại bỏ .0 nếu là số nguyên) để khớp với "OK 300"
        if (result == (long) result) {
            return "OK " + (long) result;
        } else {
            return "OK " + result;
        }
    }
}
