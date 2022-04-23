package tv.quaint.streamlinebasevelo.commands;

import tv.quaint.streamlinebasevelo.configs.ConfigPage;
import tv.quaint.streamlinebasevelo.configs.SettingArgument;
import tv.quaint.streamlinebasevelo.expansions.BaseExpansion;
import tv.quaint.streamlinebasevelo.utils.FileUtils;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandPage extends ConfigPage {
    public static CommandPage INSTANCE;

    public Command command;
    public boolean enabled;
    public String base;
    public String permission;
    public List<String> aliases = new ArrayList<>();

    public CommandPage(BaseExpansion expansion, String identifier, AppendableList<SettingArgument<?>> settingArguments) {
        super(expansion, identifier, false, settingArguments, FileUtils.getCommandFile(expansion, identifier));

        base = identifier;
        permission = "streamline.command." + expansion.identifier + "." + identifier;
    }

    public CommandPage(BaseExpansion expansion, String identifier) {
        super(expansion, identifier, false, FileUtils.getCommandFile(expansion, identifier));

        base = identifier;
        permission = "streamline.command." + expansion.identifier + "." + identifier;
    }

    public CommandPage(BaseExpansion expansion, String identifier, AppendableList<SettingArgument<?>> settingArguments, List<String> aliases) {
        super(expansion, identifier, false, settingArguments, FileUtils.getCommandFile(expansion, identifier));

        base = identifier;
        permission = "streamline.command." + expansion.identifier + "." + identifier;
        this.aliases = aliases;
    }

    public CommandPage(BaseExpansion expansion, String identifier, List<String> aliases) {
        super(expansion, identifier, false, FileUtils.getCommandFile(expansion, identifier));

        base = identifier;
        permission = "streamline.command." + expansion.identifier + "." + identifier;
        this.aliases = aliases;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        INSTANCE = this;

        enabled = config.getOrSetDefault("enabled", true);
        base = config.getOrSetDefault("base", identifier);
        permission = config.getOrSetDefault("permission", "streamline.command." + identifier);
        aliases = config.getOrSetDefault("aliases", new ArrayList<>());

        moreReload();

        reloadCommand();
    }

    public abstract static class Command extends BaseCommand {
        public Command() {
            super(INSTANCE.base, INSTANCE.permission, INSTANCE.aliases);
            INSTANCE.setCommand(this);
        }
    }

    public abstract void moreReload();

    public abstract void reloadCommand();
}
