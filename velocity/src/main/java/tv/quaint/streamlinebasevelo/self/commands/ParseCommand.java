package tv.quaint.streamlinebasevelo.self.commands;

import com.velocitypowered.api.command.CommandSource;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.commands.BaseCommand;
import tv.quaint.streamlinebasevelo.commands.CommandManager;
import tv.quaint.streamlinebasevelo.commands.CommandPage;
import tv.quaint.streamlinebasevelo.configs.SettingArgument;
import tv.quaint.streamlinebasevelo.savables.users.SavableUser;
import tv.quaint.streamlinebasevelo.utils.*;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParseCommand extends CommandPage {
    public static String parseMessage;

    public ParseCommand() {
        super(StreamlineBase.EXPANSION, "parse");
    }

    @Override
    public void moreReload() {
        parseMessage = config.getOrSetDefault("messages.parse", "%streamline_prefix%&eParsed on %player_formatted% &eas&8: %to_parse%");
    }

    @Override
    public void reloadCommand() {
        this.command = new Command();
        CommandManager.registerCommand(this);
    }

    public static class Command extends CommandPage.Command {
        @Override
        public void run(CommandSource sender, String[] args) {
            if (args.length <= 1) {
                BaseMessaging.sendMessage(sender, StreamlineBase.MESSAGES.needsMore);
                return;
            }

            SavableUser as = SavableHandler.getOrGetSavableUser(UUIDUtils.getCachedUUID(args[0]));

            if (as == null) {
                BaseMessaging.sendMessage(sender, StreamlineBase.MESSAGES.noPlayer);
                return;
            }

            BaseMessaging.sendMessage(sender, parseMessage
                    .replace("%to_parse%", BaseMessaging.argsToStringMinus(args, 1)),
                            as);
        }

        @Override
        public Collection<String> onTabComplete(CommandSource sender, String[] args) {
            if (args.length == 1) {
                return MatcherUtils.getCompletion(BasePlayerHandler.getOnlinePlayerNames(), args[0]);
            }
            if (args.length >= 2) {
                List<String> options = new ArrayList<>();
                options.add("%%");
                return MatcherUtils.getCompletion(options, args[args.length - 1]);
            }

            return new ArrayList<>();
        }
    }
}
