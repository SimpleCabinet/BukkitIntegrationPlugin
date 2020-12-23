package pro.gravit.plugin.bukkitintegration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pro.gravit.launcher.request.Request;
import pro.gravit.launcher.request.management.PingServerReportRequest;
import pro.gravit.launcher.request.websockets.ClientWebSocketService;
import pro.gravit.launcher.server.ServerWrapper;
import pro.gravit.plugin.bukkitintegration.lk.event.UserItemDeliveryEvent;
import pro.gravit.plugin.bukkitintegration.lk.event.request.ChangeOrderStatusRequestEvent;
import pro.gravit.plugin.bukkitintegration.lk.event.request.FetchOrdersRequestEvent;
import pro.gravit.plugin.bukkitintegration.lk.request.ChangeOrderStatusRequest;
import pro.gravit.plugin.bukkitintegration.lk.request.FetchOrdersRequest;

import java.io.IOException;

public final class Bukkitintegration extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new PingReporter(ServerWrapper.wrapper), 20*20, 20*20);
        ClientWebSocketService.results.register("lkUserOrderDelivery", UserItemDeliveryEvent.class);
        ClientWebSocketService.results.register("lkChangeOrderStatus", ChangeOrderStatusRequestEvent.class);
        ClientWebSocketService.results.register("lkFetchOrders", FetchOrdersRequestEvent.class);
        Request.service.registerEventHandler(new IntegrationEventHandler());

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("cart")) {
            if(!sender.hasPermission("gravitlauncher.cart.base")) {
                sender.sendMessage("Permissions denied");
                return true;
            }
            FetchOrdersRequest request = new FetchOrdersRequest();
            request.filterByType = ChangeOrderStatusRequest.OrderStatus.DELIVERY;
            request.userUuid = ((Player)sender).getUniqueId();
            request.fetchSystemInfo = true;
            request.deliveryUser = true;
            boolean knownCommand = false;
            if(args.length > 1) {
                if(args[0].equals("get")) {
                    request.orderId = Long.parseLong(args[1]);
                    request.userUuid = null;
                    knownCommand = true;
                }
            }
            if(args.length > 0) {
                if(args[0].equals("all")) {
                    knownCommand = true;
                }
            }
            if(!knownCommand) {
                sender.sendMessage("Unknown subcommand");
                return true;
            }
            try {
                FetchOrdersRequestEvent result = request.request();
                for(FetchOrdersRequestEvent.PublicOrderInfo i : result.list) {
                    if(i.systemInfo == null) {
                        continue;
                    }
                    if(i.cantDelivery) {
                        continue;
                    }
                    if(i.status == ChangeOrderStatusRequest.OrderStatus.DELIVERY) {
                        int delivered = IntegrationEventHandler.processDeliveryItemToPlayer(i.orderId, (Player)sender, i.systemInfo, i.part);
                        sender.sendMessage("Вам выдан предмет по заказу номер "+ i.orderId +" в колличестве "+ delivered +" штук. Осталось "+(i.part - delivered));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
