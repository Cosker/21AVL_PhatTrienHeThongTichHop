package Lab4.src.BTDeXuat;

import java.net.InetAddress;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.net.URI;
import java.net.URISyntaxException;

public class HostUriInspector {
    public static void main(String[] args) {
        // Xử lý thiếu tham số
        if (args.length != 2) {
            System.err.println("Lỗi: Thiếu tham số.");
            System.out.println("Cú pháp đúng: java HostUriInspector <hostname> <URI>");
            return;
        }

        String hostname = args[0];
        String uriString = args[1];

        // 1. Kiểm tra và in thông tin Hostname
        System.out.println("=== THÔNG TIN HOSTNAME ===");
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            System.out.println("Hostname: " + hostname);

            for (InetAddress addr : addresses) {
                System.out.println("- IP: " + addr.getHostAddress());

                // Xác định loại IPv4 hay IPv6
                String type = "Unknown";
                if (addr instanceof Inet4Address) {
                    type = "IPv4";
                } else if (addr instanceof Inet6Address) {
                    type = "IPv6";
                }
                System.out.println("  + Loại: " + type);
                System.out.println("  + Loopback: " + addr.isLoopbackAddress());
                System.out.println("  + Site local: " + addr.isSiteLocalAddress());
            }
        } catch (UnknownHostException e) {
            System.err.println("Lỗi: Không thể phân giải hostname '" + hostname + "'");
        }

        System.out.println("\n=== THÔNG TIN URI ===");
        // 2. Kiểm tra và in thông tin URI
        try {
            URI uri = new URI(uriString);
            System.out.println("URI gốc: " + uriString);
            System.out.println("- Scheme: " + uri.getScheme());
            System.out.println("- Host: " + uri.getHost());
            System.out.println("- Port: " + uri.getPort());
            System.out.println("- Path: " + uri.getPath());
            System.out.println("- Query: " + uri.getQuery());
            System.out.println("- Fragment: " + uri.getFragment());
        } catch (URISyntaxException e) {
            System.err.println("Lỗi: Cú pháp URI không hợp lệ. Chi tiết: " + e.getMessage());
        }
    }
}