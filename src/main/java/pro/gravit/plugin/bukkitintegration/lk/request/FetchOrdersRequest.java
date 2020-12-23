package pro.gravit.plugin.bukkitintegration.lk.request;

import pro.gravit.launcher.request.Request;
import pro.gravit.plugin.bukkitintegration.lk.event.request.FetchOrdersRequestEvent;

import java.util.UUID;

public class FetchOrdersRequest extends Request<FetchOrdersRequestEvent> {
    public String userUsername;
    public UUID userUuid;
    public int userId;

    public long lastId;
    public ChangeOrderStatusRequest.OrderStatus filterByType;
    public long orderId;

    public boolean fetchSystemInfo;
    public boolean deliveryUser;
    @Override
    public String getType() {
        return "lkFetchOrders";
    }
}
