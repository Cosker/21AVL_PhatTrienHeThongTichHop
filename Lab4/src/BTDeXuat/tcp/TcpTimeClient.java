package Lab4.src.BTDeXuat.tcp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TcpTimeClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
                Scanner scanner = new Scanner(System.in, "UTF-8")) {

            while (true) {
                System.out.print("Nhập lệnh (DATE, TIME, DATETIME, QUIT): ");
                String cmd = scanner.nextLine();
                out.println(cmd);

                if (cmd.equalsIgnoreCase("QUIT")) {
                    System.out.println("Đã kết thúc dịch vụ ngày giờ TCP!");
                    break;
                }
                System.out.println("Server: " + in.readLine());
            }
        } catch (IOException e) {
            System.err.println("Mất kết nối với Server: " + e.getMessage());
        }
    }
}
