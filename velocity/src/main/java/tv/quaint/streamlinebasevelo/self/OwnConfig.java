package tv.quaint.streamlinebasevelo.self;

import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.configs.ConfigPage;
import tv.quaint.streamlinebasevelo.configs.SettingArgument;
import tv.quaint.streamlinebasevelo.databases.DatabaseConfiguration;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

public class OwnConfig extends ConfigPage {
    public boolean debugEnabled;
    public boolean databaseEnabled;
    public DatabaseConfiguration.SupportedType databaseType;
    public DatabaseConfiguration databaseConfiguration;

    public boolean textUpdateDisplayName;

    public OwnConfig() {
        super(StreamlineBase.EXPANSION, "config", true, new AppendableList<>());
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        debugEnabled = config.getOrSetDefault("debug", false);
        databaseEnabled = config.getOrSetDefault("database.enabled", false);
        databaseType = DatabaseConfiguration.SupportedType.valueOf(config.getOrSetDefault("database.type", DatabaseConfiguration.SupportedType.MONGO.name()));

        if (databaseEnabled) {
            databaseConfiguration = new DatabaseConfiguration(databaseType, StreamlineBase.EXPANSION, "database-info", false, new AppendableList<>(new SettingArgument<>(databaseType))) {
                @Override
                public void onMoreReload() {
                    // do nothing
                }
            };
        }

        textUpdateDisplayName = config.getOrSetDefault("text.update-display-names", true);
    }
}
