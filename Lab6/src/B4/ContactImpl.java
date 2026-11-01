package Lab6.src.B4;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ContactImpl extends UnicastRemoteObject implements ContactInterface {

    // Sử dụng HashMap để lưu trữ danh bạ trên RAM
    private HashMap<String, String> phonebook;

    // Constructor khởi tạo HashMap và ném ra RemoteException
    public ContactImpl() throws RemoteException {
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
            System.out.println("[Log] Xóa thành công liên hệ: " + name);
            return true;
        }
        System.out.println("[Log] Xóa thất bại (không tìm thấy liên hệ).");
        return false;
    }
}
