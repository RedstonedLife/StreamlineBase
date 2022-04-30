package tv.quaint.streamlinebase.self.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.self.pages.JoinPage;
import tv.quaint.streamlinebase.utils.BaseMessaging;
import tv.quaint.streamlinebase.utils.BasePlayerHandler;
import tv.quaint.streamlinebase.utils.SavableHandler;

public class BaseListener {
    public BaseListener() {
        StreamlineBase.SERVER.getEventManager().register(StreamlineBase.INSTANCE, this);
        BaseMessaging.logInfo("BaseListener registered!");
    }

    @Subscribe
    public void onJoin(ServerConnectedEvent event) {
        Player player = event.getPlayer();

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
