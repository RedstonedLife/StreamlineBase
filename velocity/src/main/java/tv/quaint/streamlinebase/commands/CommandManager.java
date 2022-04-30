package tv.quaint.streamlinebase.commands;

import com.velocitypowered.api.command.CommandMeta;
import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.utils.BaseMessaging;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public static ConcurrentHashMap<CommandPage, BaseCommand> loadedCommands = new ConcurrentHashMap<>();

    public static CommandMeta getCommandMeta(BaseCommand command) {
        CommandMeta meta = StreamlineBase.SERVER.getCommandManager().getCommandMeta(command.base);
        if (meta != null) return meta;

        return StreamlineBase.SERVER.getCommandManager().metaBuilder(command.base)
                .aliases(command.aliases.toArray(new String[0]))
                .build();
    }

    public static void registerCommand(CommandPage page) {
        if (! page.enabled) return;

        StreamlineBase.SERVER.getCommandManager().register(
                getCommandMeta(page.command),
                page.command
        );
        loadedCommands.put(page, page.command);
        BaseMessaging.logInfo("Registered command: " + page.command.base + " | With aliases: " + BaseMessaging.getStringFromStringList(page.command.aliases));
    }

    public static void unregisterCommand(CommandPage page) {
        StreamlineBase.SERVER.getCommandManager().unregister(
                getCommandMeta(page.command)
        );
        loadedCommands.remove(page);
    }

    public static void reloadCommands() {
        for (CommandPage page : loadedCommands.keySet()) {
            unregisterCommand(page);

            page.reload();
        }
    }
}
