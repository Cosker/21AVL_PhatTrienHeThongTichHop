package Lab4.src.BTDeXuat.calc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CalcClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 7000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                Scanner scanner = new Scanner(System.in, "UTF-8")) {

            System.out.println("Đã kết nối tới Server Máy tính. Nhập lệnh (VD: CALC + 100 200) hoặc QUIT để thoát.");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine();

                out.println(input);

                if (input.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }

                String response = in.readLine();
                System.out.println("Server: " + response);
            }
        } catch (IOException e) {
            System.err.println("Không thể kết nối tới Server: " + e.getMessage());
        }
    }
}
