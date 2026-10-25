package Lab6.src.B4;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

public class ContactServer extends UnicastRemoteObject implements ContactInterface {

    // Sử dụng HashMap để lưu trữ danh bạ (Key: Tên, Value: Số điện thoại)
    private HashMap<String, String> phonebook;

    protected ContactServer() throws RemoteException {
        super();
        phonebook = new HashMap<>();
    }

    @Override
    public void addContact(String name, String phone) throws RemoteException {
        phonebook.put(name, phone);
        System.out.println("[Log] Đã thêm/cập nhật liên hệ: " + name + " - " + phone);
    }

    @Override
    public String findContact(String name) throws RemoteException {
        System.out.println("[Log] Nhận yêu cầu tìm kiếm liên hệ: " + name);
        // Trả về số điện thoại nếu tìm thấy, ngược lại trả về null
        return phonebook.get(name);
    }

    @Override
    public boolean deleteContact(String name) throws RemoteException {
        System.out.println("[Log] Nhận yêu cầu xóa liên hệ: " + name);
        if (phonebook.containsKey(name)) {
            phonebook.remove(name);
            return true; // Xóa thành công
        }
        return false; // Không tìm thấy tên để xóa
    }

    public static void main(String[] args) {
        try {
            // Mở RMI Registry ở port 1099
            LocateRegistry.createRegistry(1099);

            // Khởi tạo server và đăng ký với tên "ContactService"
            ContactServer server = new ContactServer();
            Naming.rebind("rmi://localhost:1099/ContactService", server);

            System.out.println("Contact Server đang chạy và chờ kết nối...");
        } catch (Exception e) {
            System.err.println("Lỗi Server: " + e.toString());
            e.printStackTrace();
        }
    }
}
