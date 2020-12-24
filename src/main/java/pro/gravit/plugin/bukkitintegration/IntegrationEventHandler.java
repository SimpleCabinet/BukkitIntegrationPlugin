package pro.gravit.plugin.bukkitintegration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pro.gravit.launcher.request.Request;
import pro.gravit.launcher.request.WebSocketEvent;
import pro.gravit.launcher.request.websockets.ClientWebSocketService;
import pro.gravit.plugin.bukkitintegration.lk.event.UserItemDeliveryEvent;
import pro.gravit.plugin.bukkitintegration.lk.request.ChangeOrderStatusRequest;
import pro.gravit.utils.helper.LogHelper;

import java.io.IOException;

public class IntegrationEventHandler implements ClientWebSocketService.EventHandler {
    @Override
    public <T extends WebSocketEvent> boolean eventHandle(T event) {

        if(event instanceof UserItemDeliveryEvent)
        {
            UserItemDeliveryEvent deliveryEvent = (UserItemDeliveryEvent) event;
            Player player = Bukkit.getPlayer(deliveryEvent.userUuid);
            if(player == null) {
                /*LogHelper.info("Delivery order %d paused - player %s not online", deliveryEvent.orderId, deliveryEvent.userUsername);
                ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(deliveryEvent.orderId, ChangeOrderStatusRequest.OrderStatus.DELIVERY);
                try {
                    Request.service.request(request);
                } catch (IOException e) {
                    LogHelper.error(e);
                }*/
            }
            else {
                processDeliveryItemToPlayer(deliveryEvent.orderId, player, deliveryEvent.data, deliveryEvent.part);
            }
        }
        return false;
    }
    public static int processDeliveryItemToPlayer(long orderId, Player player, UserItemDeliveryEvent.OrderSystemInfo orderSystemInfo, int part) {
        int rejectedPart = ItemDeliveryHelper.deliveryItemToPlayer(player, orderSystemInfo, part);
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(orderId, ChangeOrderStatusRequest.OrderStatus.DELIVERY);
        if(rejectedPart == 0) {
            request.status = ChangeOrderStatusRequest.OrderStatus.FINISHED;
        }
        else {
            request.isParted = true;
            request.part = rejectedPart;
        }
        try {
            Request.service.request(request).exceptionally((e) -> {
                LogHelper.error(e);
                return null;
            });
        } catch (IOException e) {
            LogHelper.error(e);
        }
        LogHelper.dev("Checked part: %d", rejectedPart);
        return part - rejectedPart;
    }


}
