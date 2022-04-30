package tv.quaint.streamlinebase.self.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.commands.CommandManager;
import tv.quaint.streamlinebase.commands.CommandPage;
import tv.quaint.streamlinebase.savables.users.SavablePlayer;
import tv.quaint.streamlinebase.utils.*;

import java.util.ArrayList;
import java.util.Collection;

public class ReloadCommand extends CommandPage {
    public static String reloadMessage;

    public ReloadCommand() {
        super(StreamlineBase.EXPANSION, "streamline-reload");
    }

    @Override
    public void moreReload() {
        reloadMessage = config.getOrSetDefault("messages.reload", "%streamline_prefix%&eSuccessfully &a&lreloaded&8!");
    }

    @Override
    public void reloadCommand() {
        this.command = new Command();
        CommandManager.registerCommand(this);
    }

    public static class Command extends CommandPage.Command {
        @Override
        public void run(CommandSource sender, String[] args) {
            StreamlineBase.CONFIG.reload();
            StreamlineBase.MESSAGES.reload();
            StreamlineBase.SAVABLES.reload();
            CommandManager.reloadCommands();

            for (Player player : BasePlayerHandler.getOnlinePlayers()) {
                SavablePlayer sp = SavableHandler.addUser(player);
                BasePlayerHandler.updateDisplayName(sp);
            }

            BaseMessaging.sendMessage(sender, reloadMessage);
        }

        @Override
        public Collection<String> onTabComplete(CommandSource sender, String[] args) {
            return new ArrayList<>();
        }
    }
}
