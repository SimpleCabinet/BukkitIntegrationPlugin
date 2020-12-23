package pro.gravit.plugin.bukkitintegration.lk.event;

import pro.gravit.launcher.request.WebSocketEvent;

import java.util.List;
import java.util.UUID;

public class UserItemDeliveryEvent implements WebSocketEvent {
    public long orderId;
    public String userUsername;
    public UUID userUuid;
    public int part;
    public static class OrderSystemInfo {
        public String itemId;
        public String itemExtra;
        public static class OrderSystemEnchantInfo {
            public String name;
            public int level;
        }
        public List<OrderSystemEnchantInfo> enchants;
        public String itemNbt;
    }
    public OrderSystemInfo data;
    @Override
    public String getType() {
        return "lkUserOrderDelivery";
    }
}
