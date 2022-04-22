package tv.quaint.streamlinebasevelo.configs;

import de.leonhard.storage.Config;
import tv.quaint.streamlinebasevelo.expansions.BaseExpansion;
import tv.quaint.streamlinebasevelo.utils.FileUtils;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

import java.io.File;

public abstract class ConfigPage {
    public Config config;
    public BaseExpansion expansion;
    public String identifier;
    public File file;
    public boolean hasDefault;
    public AppendableList<SettingArgument<?>> settingArguments;

    public ConfigPage(BaseExpansion expansion, String identifier, boolean hasDefault, AppendableList<SettingArgument<?>> settingArguments) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = FileUtils.getExpansionPage(expansion, identifier);
        this.hasDefault = hasDefault;
        this.settingArguments = settingArguments;

        reload();
    }

    public Config loadConfig() {
        if (this.hasDefault) {
            return FileUtils.loadConfigFromSelf(file, file.getName());
        } else {
            return FileUtils.loadConfigNoDefault(file);
        }
    }

    public void reload() {
        config = loadConfig();

        onReload(settingArguments);
    }

    public abstract void onReload(AppendableList<SettingArgument<?>> settingArguments);
}
