package tv.quaint.streamlinebase.savables;

import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.configs.ConfigPage;
import tv.quaint.streamlinebase.configs.SettingArgument;
import tv.quaint.streamlinebase.utils.FileUtils;
import tv.quaint.streamlinebase.utils.obj.AppendableList;

import java.io.File;

public abstract class SavableFile extends ConfigPage {
    public String uuid;
    public boolean enabled;
    public boolean firstLoad;
    public SavableAdapter.SavableType type;

    public String parseFileName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }

    public SavableFile(String uuid, SavableAdapter.SavableType type) {
        super(StreamlineBase.EXPANSION, uuid, false, new AppendableList<>(),  FileUtils.getCorrectSavableTypeFile(uuid, type));
    }

    public SavableFile(String uuid, SavableAdapter.SavableType type, File file) {
        super(StreamlineBase.EXPANSION, uuid, false, new AppendableList<>(), file);

        this.type = type;

        this.enabled = true;
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        this.populateDefaults();

        this.loadValues();
    }

    abstract public void populateDefaults();

    public <T> T getOrSetDefault(String key, T def) {
        return this.config.getOrSetDefault(key, def);
    }

    abstract public void loadValues();

    abstract public void saveAll();

    public void set(final String key, final Object value) {
        this.config.set(key, value);
    }

    public String toString() {
        return "[ File: " + file.getName() + " , UUID: " + this.uuid + " ]";
    }

    public void dispose() throws Throwable {
        this.uuid = null;
        this.finalize();
    }
}
