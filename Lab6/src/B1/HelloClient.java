package Lab6.src.B1;

import java.rmi.Naming;

public class HelloClient {
    public static void main(String[] args) {
        try {
            // Tìm đối tượng từ xa theo URL đăng ký trên server
            HelloInterface hello = (HelloInterface) Naming.lookup("rmi://localhost/HelloService");

            // Gọi phương thức từ xa
            String response = hello.sayHello();

            // Hiển thị kết quả nhận được
            System.out.println("Thông điệp từ Server: " + response);

        } catch (Exception e) {
            System.err.println("Lỗi Client: " + e.toString());
            e.printStackTrace();
        }
    }
}
