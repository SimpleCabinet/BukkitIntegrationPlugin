package pro.gravit.plugin.bukkitintegration.lk.request;


import pro.gravit.launcher.request.Request;
import pro.gravit.plugin.bukkitintegration.lk.event.request.ChangeOrderStatusRequestEvent;

public class ChangeOrderStatusRequest extends Request<ChangeOrderStatusRequestEvent> {
    public enum OrderStatus
    {
        CREATED, PROCESS, DELIVERY, FINISHED, FAILED
    }
    public long orderId;
    public OrderStatus status;
    public boolean isParted;
    public int part;

    public ChangeOrderStatusRequest(long orderId, OrderStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    @Override
    public String getType() {
        return "lkChangeOrderStatus";
    }
}
