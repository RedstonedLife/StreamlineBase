package tv.quaint.streamlinebasevelo.databases;

import tv.quaint.streamlinebasevelo.configs.ConfigPage;
import tv.quaint.streamlinebasevelo.configs.SettingArgument;
import tv.quaint.streamlinebasevelo.expansions.BaseExpansion;
import tv.quaint.streamlinebasevelo.utils.obj.AppendableList;

import java.io.File;

public abstract class DatabaseConfiguration extends ConfigPage {
    public enum SupportedType {
        MONGO("mongodb://%username%:%password%@%ip%:%port%/%database%", 27017),
        MYSQL("jdbc:mysql://%username%:%password%@%ip%:%port%/%database%", 3306),
        LOCAL("local", 0),

        NULL("null", 0)
        ;

        public final String formatDefault;
        public final int portDefault;

        SupportedType(String formatDefault, int portDefault) {
            this.formatDefault = formatDefault;
            this.portDefault = portDefault;
        }

        public String defaultPort() {
            return String.valueOf(this.portDefault);
        }
    }

    public String database; // Also known as the database on MySQL.
    public String ip;
    public String port;
    public String username;
    public String password;
    public String connectionString;

    public SupportedType of = SupportedType.NULL;

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, String identifier, boolean hasDefault, AppendableList<SettingArgument<?>> settingArguments, File file) {
        super(expansion, identifier, hasDefault, new AppendableList<>(settingArguments, new SettingArgument<>(of)), file);
        this.of = of;
    }

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, String identifier, boolean hasDefault, AppendableList<SettingArgument<?>> settingArguments) {
        super(expansion, identifier, hasDefault, new AppendableList<>(settingArguments, new SettingArgument<>(of)));
        this.of = of;
    }

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, String identifier, AppendableList<SettingArgument<?>> settingArguments) {
        this(of, expansion, identifier, true, settingArguments);
    }

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, AppendableList<SettingArgument<?>> settingArguments) {
        this(of, expansion, "database-info", false, settingArguments);
    }

    // With no appendables:

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, String identifier, boolean hasDefault) {
        this(of, expansion, identifier, hasDefault, new AppendableList<>());
    }

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion, String identifier) {
        this(of, expansion, identifier, new AppendableList<>());
    }

    public DatabaseConfiguration(SupportedType of, BaseExpansion expansion) {
        this(of, expansion, new AppendableList<>());
    }

    @Override
    public void onReload(AppendableList<SettingArgument<?>> settingArguments) {
        SettingArgument<?> settingArgument = settingArguments.list.get(0);
        if (settingArgument.isOfType(SupportedType.NULL)) this.of = (SupportedType) settingArgument.type;
        if (! this.of.equals(SupportedType.LOCAL)) {
            this.database = config.getOrSetDefault("database", "");
            this.ip = config.getOrSetDefault("ip", "");
            this.port = config.getOrSetDefault("port", "");
            if (this.of.equals(SupportedType.MONGO)) this.port = SupportedType.MONGO.defaultPort();
            if (this.of.equals(SupportedType.MYSQL)) this.port = SupportedType.MYSQL.defaultPort();
            this.username = config.getOrSetDefault("username", "");
            this.password = config.getOrSetDefault("password", "");
            this.connectionString = config.getOrSetDefault("connection-string", "");
            if (this.of.equals(SupportedType.MONGO)) this.connectionString = SupportedType.MONGO.formatDefault;
            if (this.of.equals(SupportedType.MYSQL)) this.connectionString = SupportedType.MYSQL.formatDefault;
        }

        onMoreReload();
    }

    abstract public void onMoreReload();

    public void loadDatabase() {
        if (this.of.equals(SupportedType.MONGO)) MongoHandler.loadDatabase(this);
    }

    public String getUri() {
        return this.connectionString
                .replace("%username%", this.username)
                .replace("%password%", this.password)
                .replace("%ip%", this.ip)
                .replace("%port%", this.port)
                .replace("%database%", this.database)
                ;
    }
}
