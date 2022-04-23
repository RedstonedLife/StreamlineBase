package tv.quaint.streamlinebasevelo.self.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.savables.users.SavablePlayer;
import tv.quaint.streamlinebasevelo.self.pages.JoinPage;
import tv.quaint.streamlinebasevelo.utils.BaseMessaging;
import tv.quaint.streamlinebasevelo.utils.BasePlayerHandler;
import tv.quaint.streamlinebasevelo.utils.SavableHandler;

public class Listener {
    @Subscribe
    public void onJoin(ServerConnectedEvent event) {
        Player player = event.getPlayer();

        SavablePlayer sp = SavableHandler.addUser(player);

        JoinPage page = new JoinPage(player);
        page.updateLatest();

        if (StreamlineBase.CONFIG.textUpdateDisplayName) {
            BasePlayerHandler.updateDisplayName(sp);
        }
    }
}
