package Lab3.src.B6TongHop;

public class Product {
    private String code;
    private String name;
    private double unitPrice;
    private int quantity;

    // Constructor kiểm tra dữ liệu đầu vào
    public Product(String code, String name, double unitPrice, int quantity) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Mã không được rỗng.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được rỗng.");
        }
        if (unitPrice <= 0) {
            throw new IllegalArgumentException("Giá đơn phải lớn hơn 0.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Số lượng không được âm.");
        }
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public double getInventoryValue() {
        return unitPrice * quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    // Phương thức hỗ trợ tạo chuỗi để ghi vào CSV
    public String toCsvRow() {
        return code + "," + name + "," + unitPrice + "," + quantity;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %,.0f VNĐ", code, name, getInventoryValue());
    }
}
