package pro.gravit.plugin.bukkitintegration.lk.event.request;

import pro.gravit.launcher.events.RequestEvent;
import pro.gravit.plugin.bukkitintegration.lk.event.UserItemDeliveryEvent;
import pro.gravit.plugin.bukkitintegration.lk.request.ChangeOrderStatusRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FetchOrdersRequestEvent extends RequestEvent {
    public static class PublicOrderInfo {
        public long orderId;
        public long productId;
        public LocalDateTime date;
        public ChangeOrderStatusRequest.OrderStatus status;
        public UserItemDeliveryEvent.OrderSystemInfo systemInfo;
        public int part;
        public String userUsername;
        public UUID userUUID;
        public boolean cantDelivery;
    }
    public List<PublicOrderInfo> list;

    public FetchOrdersRequestEvent(List<PublicOrderInfo> list) {
        this.list = list;
    }

    @Override
    public String getType() {
        return "lkFetchOrders";
    }
}
