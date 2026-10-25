import java.sql.*;

public class BTTH_JDBC {
    private static final String URL = "jdbc:sqlite:nhanvien.db";

    public static void main(String[] args) {
        // Khởi tạo các bảng dữ liệu
        initDatabase();

        // BÀI TẬP 1 & 4: Quản lý Sản phẩm (CRUD) bằng PreparedStatement
        System.out.println("=== BÀI TẬP 1 & 4: QUẢN LÝ SẢN PHẨM (PREPAREDSTATEMENT) ===");

        // 1. Create (Thêm mới 3 sản phẩm)
        themSanPham("SP01", "Laptop Dell", 15000000, 10);
        themSanPham("SP02", "Chuột Logitech", 450000, 50);
        themSanPham("SP03", "Bàn phím Cơ", 1200000, 20);

        // 2. Read (Liệt kê tất cả sản phẩm)
        hienThiDanhSachSanPham();

        // Tìm kiếm sản phẩm theo tên
        timKiemSanPhamTheoTen("Laptop");

        // 3. Update (Cập nhật giá hoặc số lượng theo maSP)
        capNhatSanPham("SP01", 14500000, 8);

        // 4. Delete (Xóa một sản phẩm)
        xoaSanPham("SP03");
        hienThiDanhSachSanPham();

        // BÀI TẬP 2 & 3: Sử dụng Transaction & Catch/Finally Triệt Để
        System.out.println("\n=== BÀI TẬP 2 & 3: GIAO DỊCH (TRANSACTION) VÀ XỬ LÝ NGOẠI LỆ ===");
        thucHienGiaoDichChuyenKho();

        // BÀI TẬP 5: Truy vấn dữ liệu nâng cao (Aggregation Functions)
        System.out.println("\n=== BÀI TẬP 5: THỐNG KÊ LƯƠNG NHÂN VIÊN ===");
        thongKeLuongNhanVien();
    }

    // Khởi tạo bảng SanPham và NhanVien
    private static void initDatabase() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(URL);
            stmt = conn.createStatement();

            // Tạo bảng SanPham (maSP, tenSP, gia, soluong)
            String sql_create_sanpham = "CREATE TABLE IF NOT EXISTS SanPham (" +
                    "maSP TEXT PRIMARY KEY, " +
                    "tenSP TEXT NOT NULL, " +
                    "gia REAL, " +
                    "soluong INTEGER)";
            stmt.executeUpdate(sql_create_sanpham);

            // Tạo bảng NhanVien với cột luong
            String sql_create_nhanvien = "CREATE TABLE IF NOT EXISTS nhanvien (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ten TEXT NOT NULL, " +
                    "chuc_vu TEXT, " +
                    "luong REAL)";
            stmt.executeUpdate(sql_create_nhanvien);

        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Lỗi khởi tạo CSDL: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    // BÀI TẬP 1 & 4: CRUD SẢN PHẨM BẰNG PREPAREDSTATEMENT
    public static void themSanPham(String maSP, String tenSP, double gia, int soLuong) {
        String sql = "INSERT OR REPLACE INTO SanPham(maSP, tenSP, gia, soluong) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(URL);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSP);
            pstmt.setString(2, tenSP);
            pstmt.setDouble(3, gia);
            pstmt.setInt(4, soLuong);
            pstmt.executeUpdate();
            System.out.println("-> Đã thêm/cập nhật sản phẩm: " + tenSP);
        } catch (SQLException e) {
            System.err.println("Lỗi thêm sản phẩm: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public static void hienThiDanhSachSanPham() {
        String sql = "SELECT maSP, tenSP, gia, soluong FROM SanPham";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            System.out.println("\n--- DANH SÁCH SẢN PHẨM ---");
            while (rs.next()) {
                System.out.printf("Mã: %-6s | Tên: %-20s | Giá: %,12.0f VNĐ | SL: %d%n",
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getDouble("gia"),
                        rs.getInt("soluong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi hiển thị sản phẩm: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    public static void timKiemSanPhamTheoTen(String tuKhoa) {
        String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + tuKhoa + "%");
            rs = pstmt.executeQuery();
            System.out.println("\n--- KẾT QUẢ TÌM KIẾM TÊN ('" + tuKhoa + "') ---");
            while (rs.next()) {
                System.out.printf("Mã: %s | Tên: %s | Giá: %,.0f VNĐ%n",
                        rs.getString("maSP"), rs.getString("tenSP"), rs.getDouble("gia"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    public static void capNhatSanPham(String maSP, double giaMoi, int soLuongMoi) {
        String sql = "UPDATE SanPham SET gia = ?, soluong = ? WHERE maSP = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(URL);
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, giaMoi);
            pstmt.setInt(2, soLuongMoi);
            pstmt.setString(3, maSP);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n-> Đã cập nhật giá và số lượng cho mã SP: " + maSP);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    public static void xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(URL);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSP);
            pstmt.executeUpdate();
            System.out.println("\n-> Đã xóa thành công sản phẩm: " + maSP);
        } catch (SQLException e) {
            System.err.println("Lỗi xóa sản phẩm: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // BÀI TẬP 2 & 3: SỬ DỤNG GIAO DỊCH (TRANSACTION) VÀ FINALLY TRIỆT ĐỂ
    public static void thucHienGiaoDichChuyenKho() {
        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;

        String sqlTruKho = "UPDATE SanPham SET soluong = soluong - ? WHERE maSP = ?";
        String sqlCongKho = "UPDATE SanPham SET soluong = soluong + ? WHERE maSP = ?";

        try {
            conn = DriverManager.getConnection(URL);

            // Tắt auto-commit để bắt đầu Transaction
            conn.setAutoCommit(false);

            // Thao tác 1: Trừ số lượng SP01
            pstmt1 = conn.prepareStatement(sqlTruKho);
            pstmt1.setInt(1, 2);
            pstmt1.setString(2, "SP01");
            pstmt1.executeUpdate();

            // Thao tác 2: Cộng số lượng SP02
            pstmt2 = conn.prepareStatement(sqlCongKho);
            pstmt2.setInt(1, 2);
            pstmt2.setString(2, "SP02");
            pstmt2.executeUpdate();

            // Nếu không có lỗi xảy ra -> Xác nhận giao dịch
            conn.commit();
            System.out.println("-> Thực hiện giao dịch chuyển kho thành công (Commit)!");

        } catch (SQLException e) {
            System.err.println("-> Có lỗi xảy ra trong giao dịch! Đang Rollback... Lý do: " + e.getMessage());
            if (conn != null) {
                try {
                    // Khôi phục lại trạng thái cũ
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Lỗi khi Rollback: " + ex.getMessage());
                }
            }
        } finally {
            // Đảm bảo bật lại AutoCommit và đóng tài nguyên an toàn
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeResources(null, pstmt1, null);
            closeResources(conn, pstmt2, null);
        }
    }

    // BÀI TẬP 5: TRUY VẤN NÂNG CAO (AGGREGATION FUNCTIONS)
    public static void thongKeLuongNhanVien() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL);
            stmt = conn.createStatement();

            // Chuẩn bị dữ liệu nhân viên
            stmt.executeUpdate("DELETE FROM nhanvien");
            stmt.executeUpdate(
                    "INSERT INTO nhanvien(ten, chuc_vu, luong) VALUES('Nguyễn Văn A', 'Truong Phong', 25000000)");
            stmt.executeUpdate("INSERT INTO nhanvien(ten, chuc_vu, luong) VALUES('Trần Thị B', 'Nhan Vien', 12000000)");
            stmt.executeUpdate("INSERT INTO nhanvien(ten, chuc_vu, luong) VALUES('Lê Văn C', 'Nhan Vien', 15000000)");
            stmt.executeUpdate("INSERT INTO nhanvien(ten, chuc_vu, luong) VALUES('Phạm Văn D', 'Ke Toan', 18000000)");

            // 1. Tính Tổng, Trung bình, Min, Max
            String sqlAggregate = "SELECT SUM(luong) AS tong_luong, AVG(luong) AS luong_tb, " +
                    "MIN(luong) AS luong_min, MAX(luong) AS luong_max FROM nhanvien";
            rs = stmt.executeQuery(sqlAggregate);

            if (rs.next()) {
                System.out.printf("Tổng lương công ty    : %,15.0f VNĐ%n", rs.getDouble("tong_luong"));
                System.out.printf("Lương trung bình      : %,15.0f VNĐ%n", rs.getDouble("luong_tb"));
                System.out.printf("Mức lương thấp nhất   : %,15.0f VNĐ%n", rs.getDouble("luong_min"));
                System.out.printf("Mức lương cao nhất    : %,15.0f VNĐ%n", rs.getDouble("luong_max"));
            }
            rs.close();

            // 2. Đếm số lượng nhân viên theo chức vụ (COUNT & GROUP BY)
            String sqlGroupBy = "SELECT chuc_vu, COUNT(*) AS so_luong FROM nhanvien GROUP BY chuc_vu";
            rs = stmt.executeQuery(sqlGroupBy);
            System.out.println("\n--- ĐẾM SỐ NHÂN VIÊN THEO CHỨC VỤ ---");
            while (rs.next()) {
                System.out.printf("Chức vụ: %-15s | Số lượng: %d người%n",
                        rs.getString("chuc_vu"), rs.getInt("so_luong"));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi thống kê: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
    }

    // HÀM ĐÓNG TÀI NGUYÊN AN TOÀN TRONG KHỐI FINALLY
    private static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Lỗi đóng tài nguyên: " + e.getMessage());
        }
    }
}