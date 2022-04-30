package tv.quaint.streamlinebase.commands;

import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.utils.BaseMessaging;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public static ConcurrentHashMap<CommandPage, BaseCommand> loadedCommands = new ConcurrentHashMap<>();

    public static void registerCommand(CommandPage page) {
        if (! page.enabled) return;

        StreamlineBase.SERVER.getPluginManager().registerCommand(
                StreamlineBase.INSTANCE,
                page.command
        );
        loadedCommands.put(page, page.command);
        BaseMessaging.logInfo("Registered command: " + page.command.base + " | With aliases: " + BaseMessaging.getStringFromStringList(page.command.aliases));
    }

    public static void unregisterCommand(CommandPage page) {
        StreamlineBase.SERVER.getPluginManager().unregisterCommand(
                page.command
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
