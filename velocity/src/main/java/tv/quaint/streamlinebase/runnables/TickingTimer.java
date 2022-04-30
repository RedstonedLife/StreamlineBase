package tv.quaint.streamlinebase.runnables;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
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
        for (Player player : BasePlayerHandler.getOnlinePlayers()) {
            SavablePlayer sp = SavableHandler.getOrGetSavablePlayer(player);

            sp.updateOnline();
            if (StreamlineBase.CONFIG.textUpdateDisplayName) {
                BasePlayerHandler.updateDisplayName(sp);
            }
            sp.addPlaySecond(1);
            sp.setLatestIP(player);
            sp.setLatestName(player.getUsername());
            sp.addName(player.getUsername());

            if (player.getCurrentServer().isPresent()) {
                ServerConnection server = player.getCurrentServer().get();
                sp.setLatestServer(server.getServerInfo().getName());
            }

            sp.saveAll();
        }
    }

    public void playerXPDishOut() {
        for (Player player : BasePlayerHandler.getOnlinePlayers()) {
            SavablePlayer sp = SavableHandler.getOrGetSavablePlayer(player);

            sp.addTotalXP(StreamlineBase.SAVABLES.playerXPGiveAmount);

            sp.saveAll();
        }
    }
}
