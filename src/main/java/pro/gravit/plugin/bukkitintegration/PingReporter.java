package pro.gravit.plugin.bukkitintegration;

import org.bukkit.Bukkit;
import pro.gravit.launcher.request.Request;
import pro.gravit.launcher.request.management.PingServerReportRequest;
import pro.gravit.launcher.server.ServerWrapper;

import java.io.IOException;

public class PingReporter implements Runnable {
    private final ServerWrapper serverWrapper;

    public PingReporter(ServerWrapper serverWrapper) {
        this.serverWrapper = serverWrapper;
    }

    @Override
    public void run() {
        int maxPlayers = Bukkit.getMaxPlayers();
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        PingServerReportRequest.PingServerReport report = new PingServerReportRequest.PingServerReport(serverWrapper.config.serverName, maxPlayers, onlinePlayers);
        PingServerReportRequest request = new PingServerReportRequest(serverWrapper.config.serverName, report);
        try {
            Request.service.request(request).thenAccept((e) -> {
            }).exceptionally((e) -> {
                return null;
            });
        } catch (IOException e) {
        }
    }
}
