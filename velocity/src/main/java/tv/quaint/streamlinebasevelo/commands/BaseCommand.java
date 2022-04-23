package tv.quaint.streamlinebasevelo.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class BaseCommand implements SimpleCommand {
    public String base;
    public String permission;
    public List<String> aliases;

    public BaseCommand(String base, String permission, List<String> aliases) {
        this.base = base;
        this.permission = permission;
        this.aliases = aliases;
    }

    @Override
    public void execute(Invocation invocation) {
        run(invocation.source(), invocation.arguments());
    }

    abstract public void run(CommandSource sender, String[] args);

    abstract public Collection<String> onTabComplete(CommandSource sender, String[] args);

    public Collection<String> preTabComplete(CommandSource sender, String[] args){
        if (args == null) args = new String[]{ "" };
        if (args.length <= 0) args = new String[]{ "" };

        return onTabComplete(sender, args);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.completedFuture(new ArrayList<>(preTabComplete(invocation.source(), invocation.arguments())));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(permission);
    }
}
