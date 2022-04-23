package tv.quaint.streamlinebasevelo.self.commands;

import com.velocitypowered.api.command.CommandSource;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.commands.CommandManager;
import tv.quaint.streamlinebasevelo.commands.CommandPage;
import tv.quaint.streamlinebasevelo.utils.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ReloadCommand extends CommandPage {
    public static String reloadMessage;

    public ReloadCommand() {
        super(StreamlineBase.EXPANSION, "parse");
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

            BaseMessaging.sendMessage(sender, reloadMessage);
        }

        @Override
        public Collection<String> onTabComplete(CommandSource sender, String[] args) {
            return new ArrayList<>();
        }
    }
}
