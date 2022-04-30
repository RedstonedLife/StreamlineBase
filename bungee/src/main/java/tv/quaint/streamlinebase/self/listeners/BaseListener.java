package tv.quaint.streamlinebase.self.listeners;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.self.pages.JoinPage;
import tv.quaint.streamlinebase.utils.BaseMessaging;
import tv.quaint.streamlinebase.utils.BasePlayerHandler;
import tv.quaint.streamlinebase.utils.SavableHandler;

public class BaseListener implements Listener {
    public BaseListener() {
        StreamlineBase.SERVER.getPluginManager().registerListener(StreamlineBase.INSTANCE, this);
        BaseMessaging.logInfo("BaseListener registered!");
    }

    @EventHandler
    public void onJoin(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();

        SavablePlayer sp = SavableHandler.addUser(player);

        JoinPage page = new JoinPage(player, true);
        page.updateLatest();

        sp.updateOnline();
        if (StreamlineBase.CONFIG.textUpdateDisplayName) {
            BasePlayerHandler.updateDisplayName(sp);
        }

        sp.saveAll();
    }
}
