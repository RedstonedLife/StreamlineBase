package tv.quaint.streamlinebase.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class BaseCommand extends Command implements TabExecutor {
    public String base;
    public String permission;
    public List<String> aliases;

    public BaseCommand(String base, String permission, List<String> aliases) {
        super(base, permission, aliases.toArray(new String[0]));
        this.base = base;
        this.permission = permission;
        this.aliases = aliases;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        run(sender, args);
    }

    abstract public void run(CommandSender sender, String[] args);

    abstract public Collection<String> onTabComplete(CommandSender sender, String[] args);

    public Collection<String> preTabComplete(CommandSender sender, String[] args){
        if (args == null) args = new String[]{ "" };
        if (args.length <= 0) args = new String[]{ "" };

        return onTabComplete(sender, args);
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(permission);
    }
}
