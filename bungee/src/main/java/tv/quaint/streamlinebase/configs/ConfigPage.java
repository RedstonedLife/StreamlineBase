package tv.quaint.streamlinebase.configs;

import de.leonhard.storage.Config;
import tv.quaint.streamlinebase.expansions.BaseExpansion;
import tv.quaint.streamlinebase.utils.FileUtils;
import tv.quaint.streamlinebase.utils.obj.AppendableList;

import java.io.File;

public abstract class ConfigPage {
    public Config config;
    public BaseExpansion expansion;
    public String identifier;
    public File file;
    public boolean hasDefault;
    public AppendableList<SettingArgument<?>> settingArguments;

    public ConfigPage(BaseExpansion expansion, String identifier, boolean hasDefault) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = FileUtils.getExpansionPage(expansion, identifier);
        this.hasDefault = hasDefault;
        this.settingArguments = new AppendableList<>();

        reload();
    }

    public ConfigPage(BaseExpansion expansion, String identifier, boolean hasDefault, AppendableList<SettingArgument<?>> settingArguments) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = FileUtils.getExpansionPage(expansion, identifier);
        this.hasDefault = hasDefault;
        this.settingArguments = settingArguments;

        reload();
    }

    public ConfigPage(BaseExpansion expansion, String identifier, ConfigFileInput fileInput) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = fileInput.file;
        this.hasDefault = fileInput.hasDefault;
        this.settingArguments = new AppendableList<>();

        reload();
    }

    public ConfigPage(BaseExpansion expansion, String identifier, boolean hasDefault, File file) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = file;
        this.hasDefault = hasDefault;
        this.settingArguments = new AppendableList<>();

        reload();
    }

    public ConfigPage(BaseExpansion expansion, String identifier, boolean hasDefault, AppendableList<SettingArgument<?>> settingArguments, File file) {
        this.expansion = expansion;
        this.identifier = identifier;
        this.file = file;
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
