package tv.quaint.streamlinebase.databases;

public abstract class BaseDatabase {
    public enum SupportedType {
        MONGO("mongodb://%u%:%p%@%i%:%po%/%d%", 27017),
        MYSQL("jdbc:mysql://%u%:%p%@%i%:%po%/%d",3306),
        LOCAL("local", 0),
        NULL("null",0);

        public final String formatDefault;
        public final int portDefault;

        SupportedType(String formatDefault, int portDefault) {
            this.formatDefault = formatDefault;
            this.portDefault = portDefault;
        }

        public String defaultPort() {return String.valueOf(this.portDefault);}
    }

    /*
    Database - Database name in the SQL Engine
    IP / Port - IP and Port of the SQL Database (localhost:3306,localhost:27017)
    Username / Password - Username and password of the database
    Identifier - Allows for easier access from the DatabaseHolder when modules implement their own Databases
    that extend the BaseDatabase
    of (SupportedType) - SupportedType of database (MONGO, MYSQL, for now)
    */

    // If you're reading this I'll go to bed its like 1:20 AM for me,
    // and I need to maintain a good sleeping schedule,
    //If you want you can open a CodeWithMe session which unlike CodeToGether has no time limit
    String database,ip,port,username,password,identifier;
    SupportedType of;

    BaseDatabase(SupportedType of,
                String database, String ip, int port,
                String username, String password, String identifier
                )
    {
        this.database = database;
        this.ip = ip;
        this.port = String.valueOf(port);
        this.username = username;
        this.password = password;
        this.identifier = identifier;
        this.of = of;
    }

    public String getURI()
    {return of.formatDefault.replace("%u%",this.username).replace("%p%",this.password).replace("%i%",this.ip).replace("%po%",this.port).replace("%d%",this.database);}

    public String getDatabase() {return this.database;}
    public String getIP() {return this.ip;}
    public String getPort() {return this.port;}
    public String getUsername() {return this.username;}
    public String getPassword() {return this.password;}
    public String getIdentifier() {return this.identifier;}
    public SupportedType getOf() {return this.of;}
}
