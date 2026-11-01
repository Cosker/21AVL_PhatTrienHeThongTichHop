package Lab6.src.B9;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class AuctionImpl extends UnicastRemoteObject implements AuctionInterface {

    private Map<String, Double> itemPrices;
    private Map<String, String> itemBidders;
    private List<AuctionCallback> subscribers; // Danh sách Client đăng ký nhận thông báo

    public AuctionImpl() throws RemoteException {
        super();
        itemPrices = new HashMap<>();
        itemBidders = new HashMap<>();
        subscribers = new ArrayList<>();
    }

    @Override
    public synchronized void registerItem(String itemName, double startingPrice) throws RemoteException {
        if (!itemPrices.containsKey(itemName)) {
            itemPrices.put(itemName, startingPrice);
            itemBidders.put(itemName, "Chưa có");
            System.out.println("[Log] Đã thêm sản phẩm: " + itemName + " - Giá khởi điểm: " + startingPrice);

            // Bắn thông báo cho tất cả Client đang online
            broadcast("Sản phẩm mới vừa lên sàn: " + itemName + " (Giá KĐ: " + startingPrice + ")");
        }
    }

    @Override
    public synchronized String getItems() throws RemoteException {
        if (itemPrices.isEmpty())
            return "Hiện không có sản phẩm nào đang đấu giá.";
        StringBuilder sb = new StringBuilder("--- DANH SÁCH SẢN PHẨM ---\n");
        for (String item : itemPrices.keySet()) {
            sb.append("- ").append(item)
                    .append(" | Giá cao nhất hiện tại: ").append(itemPrices.get(item))
                    .append(" | Người giữ giá: ").append(itemBidders.get(item))
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public synchronized boolean placeBid(String itemName, double bidAmount, String bidderName) throws RemoteException {
        if (itemPrices.containsKey(itemName)) {
            double currentPrice = itemPrices.get(itemName);
            if (bidAmount > currentPrice) {
                itemPrices.put(itemName, bidAmount);
                itemBidders.put(itemName, bidderName);

                System.out.println("[Log] " + bidderName + " đặt giá " + bidAmount + " cho " + itemName);
                broadcast("🔔 Người dùng '" + bidderName + "' vừa thiết lập mức giá mới: " + bidAmount + " cho '"
                        + itemName + "'!");
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized void subscribe(AuctionCallback client) throws RemoteException {
        subscribers.add(client);
        System.out.println("[Log] Một người dùng mới đã tham gia phòng đấu giá.");
    }

    // Hàm nội bộ của Server dùng để gửi thông báo đồng loạt
    private void broadcast(String message) {
        Iterator<AuctionCallback> it = subscribers.iterator();
        while (it.hasNext()) {
            AuctionCallback client = it.next();
            try {
                // Gọi ngược lại hàm notifyUpdate nằm trên máy Client
                client.notifyUpdate(message);
            } catch (RemoteException e) {
                // Nếu Client đã tắt phần mềm (ngắt kết nối), xóa khỏi danh sách gửi
                it.remove();
            }
        }
    }
}
