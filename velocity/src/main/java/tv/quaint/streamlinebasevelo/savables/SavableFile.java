package tv.quaint.streamlinebasevelo.savables;

import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.databases.DatabaseConfiguration;
import tv.quaint.streamlinebasevelo.savables.users.SavableConsole;
import tv.quaint.streamlinebasevelo.savables.users.SavablePlayer;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

import java.io.File;

public abstract class SavableFile extends DatabaseConfiguration {
    public String uuid;
    public boolean enabled;
    public boolean firstLoad;
    public SavableAdapter.Type type;

    public String parseFileName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf("."));
    }


    public SavableFile(String uuid, SavableAdapter.Type type) {
        super(StreamlineBase.CONFIG.databaseConfiguration == null ? SupportedType.LOCAL : StreamlineBase.CONFIG.databaseType,
                StreamlineBase.EXPANSION, uuid, false, new AppendableList<>());
    }

    public SavableFile(String uuid, SavableAdapter.Type type, File file) {
        super(StreamlineBase.CONFIG.databaseConfiguration == null ? SupportedType.LOCAL : StreamlineBase.CONFIG.databaseType,
                StreamlineBase.EXPANSION, uuid, false, new AppendableList<>(), file);
    }

    public SavableFile(File file) {
        super(StreamlineBase.CONFIG.databaseConfiguration == null ? SupportedType.LOCAL : StreamlineBase.CONFIG.databaseType,
                StreamlineBase.EXPANSION, file.getName().replace(".yml", ""), false, new AppendableList<>(), file);

        if (this instanceof SavablePlayer) this.type = SavableAdapter.Type.PLAYER;
        if (this instanceof SavableConsole) this.type = SavableAdapter.Type.CONSOLE;

        try {
            reload();
            this.enabled = true;
        } catch (Exception e) {
            e.printStackTrace();
            this.enabled = false;
        }

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
