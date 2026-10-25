import java.sql.*;

public class HDTH_JDBC {
    public static void main(String[] args) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // B1: Chuẩn bị môi trường và thiết lập kết nối
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:nhanvien.db");

            // B2: Tạo đối tượng Statement từ Connection
            stmt = conn.createStatement();

            // B3.1: Tạo bảng NhanVien
            String sql_create_table = "CREATE TABLE nhanvien (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ten TEXT NOT NULL, " +
                    "chuc_vu TEXT)";
            // Thực thi câu lệnh tạo bảng:
            stmt.executeUpdate(sql_create_table);
            System.out.println("Tạo bảng thành công!");

            // B3.2: Thêm dữ liệu
            String sql_insert1 = "INSERT INTO nhanvien (ten, chuc_vu) VALUES ('Nguyễn Văn A', 'Trưởng phòng')";
            String sql_insert2 = "INSERT INTO nhanvien (ten, chuc_vu) VALUES ('Trần Thị B', 'Nhân viên kinh doanh')";
            String sql_insert3 = "INSERT INTO nhanvien (ten, chuc_vu) VALUES ('Lê Văn C', 'Kế toán')";

            stmt.executeUpdate(sql_insert1);
            stmt.executeUpdate(sql_insert2);
            stmt.executeUpdate(sql_insert3);
            System.out.println("Thêm 3 nhân viên vào CSDL thành công!");

            // B4: Truy vấn và hiển thị dữ liệu
            String sql_select = "SELECT id, ten, chuc_vu FROM nhanvien";
            rs = stmt.executeQuery(sql_select);

            System.out.println("\n--- DANH SÁCH NHÂN VIÊN ---");
            // Duyệt qua ResultSet bằng vòng lặp while(rs.next())
            while (rs.next()) {
                int id = rs.getInt("id");
                String ten = rs.getString("ten");
                String chucVu = rs.getString("chuc_vu");

                // In thông tin của từng nhân viên ra màn hình console
                System.out.printf("ID: %d | Tên: %-18s | Chức vụ: %s%n", id, ten, chucVu);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver SQLite (sqlite-jdbc): " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi thao tác CSDL SQL: " + e.getMessage());
        } finally {
            // B5: Đóng các tài nguyên theo thứ tự ngược lại (rs -> stmt -> conn)
            try {
                if (rs != null) {
                    rs.close(); // Đóng ResultSet trước
                }
                if (stmt != null) {
                    stmt.close(); // Đóng Statement
                }
                if (conn != null) {
                    conn.close(); // Đóng Connection sau cùng
                    System.out.println("\nĐã đóng kết nối CSDL an toàn.");
                }
            } catch (SQLException e) {
                System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
            }
        }
    }
}