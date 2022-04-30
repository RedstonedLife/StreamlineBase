package tv.quaint.streamlinebase.runnables;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.utils.BaseMessaging;
import tv.quaint.streamlinebase.utils.BasePlayerHandler;
import tv.quaint.streamlinebase.utils.SavableHandler;

public class TickingTimer implements Runnable {
    public int secondTimerCurrent = 0;
    public int secondTimerTickTill = 1000;
    public int playerGiveCurrent = 0;

    public TickingTimer() {
        BaseMessaging.logInfo("TickingTimer registered!");
    }

    @Override
    public void run() {
        if (secondTimerCurrent >= secondTimerTickTill) {
            secondTimerCurrent = 0;

            secondHasPassed();
        }
        if (playerGiveCurrent >= StreamlineBase.SAVABLES.playerXPGiveMillisecondsTill) {
            playerGiveCurrent = 0;

            playerXPDishOut();
        }

        secondTimerCurrent ++;
        playerGiveCurrent ++;
    }

    public void secondHasPassed() {
        for (ProxiedPlayer player : BasePlayerHandler.getOnlinePlayers()) {
            SavablePlayer sp = SavableHandler.getOrGetSavablePlayer(player);

            sp.updateOnline();
            if (StreamlineBase.CONFIG.textUpdateDisplayName) {
                BasePlayerHandler.updateDisplayName(sp);
            }
            sp.addPlaySecond(1);
            sp.setLatestIP(player);
            sp.setLatestName(player.getName());
            sp.addName(player.getName());

            if (player.getServer() != null) {
                Server server = player.getServer();
                sp.setLatestServer(server.getInfo().getName());
            }

            sp.saveAll();
        }
    }

    public void playerXPDishOut() {
        for (ProxiedPlayer player : BasePlayerHandler.getOnlinePlayers()) {
            SavablePlayer sp = SavableHandler.getOrGetSavablePlayer(player);

            sp.addTotalXP(StreamlineBase.SAVABLES.playerXPGiveAmount);

            sp.saveAll();
        }
    }
}
