package tv.quaint.streamlinebase.self;

import tv.quaint.streamlinebase.StreamlineBase;
import tv.quaint.streamlinebase.configs.ConfigPage;
import tv.quaint.streamlinebase.configs.SettingArgument;
import tv.quaint.streamlinebase.databases.DatabaseConfiguration;
import tv.quaint.streamlinebase.utils.obj.AppendableList;

public class OwnConfig extends ConfigPage {
    public boolean debugEnabled;
    public boolean databaseEnabled;
    public DatabaseConfiguration.SupportedType databaseType;
    public DatabaseConfiguration databaseConfiguration;
    public String joinCollection;
    public String playersCollection;
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
            joinCollection = config.getOrSetDefault("database.collections.joins", "join-info");
            playersCollection = config.getOrSetDefault("database.collections.players", "player-info");
        }

        textUpdateDisplayName = config.getOrSetDefault("text.update-display-names", true);
    }
}
