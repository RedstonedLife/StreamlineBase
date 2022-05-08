package tv.quaint.streamlinebase.databases;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.concurrent.ConcurrentHashMap;

public class DatabaseHolder {

    public static ConcurrentHashMap<String, BaseDatabase> databases = new ConcurrentHashMap<>();
    public static void putDatabase(String identifier, BaseDatabase database) {
        if (!contains(databases, identifier)) {
            databases.put(identifier,database);
            link(identifier, database);
        } else {
            // do something
        }
    }
    public static void removeDatabase(String identifier) {databases.remove(identifier);}
    public static BaseDatabase getDatabase(String identifier) {return databases.get(identifier);}
    public static boolean contains(ConcurrentHashMap<?,?> map, String identifier) {return map.containsKey(identifier);}
    public static ConcurrentHashMap<String, MongoDatabase> mongoLinker = new ConcurrentHashMap<>();
    public static void link(String identifier, BaseDatabase database) {
        if(database.getOf().equals(BaseDatabase.SupportedType.MONGO)) {
            if(!contains(mongoLinker, identifier)) {
                mongoLinker.put(identifier,new Mongo(database.getURI(), database.getDatabase()).getDatabase());
            } else {
                // do something
            }
        }
    }
    public static void unlink(String identifier, BaseDatabase database) {
        if(database.getOf().equals(BaseDatabase.SupportedType.MONGO)) {
            mongoLinker.remove(identifier);
        }
    }

    static class Mongo {
        MongoDatabase database;
        Mongo(String URI, String database) {
            MongoClientURI _uri = new MongoClientURI(URI);
            MongoClient client = new MongoClient(_uri);

            this.database = client.getDatabase(database);
        }
        MongoDatabase getDatabase() {return this.database;}
    }

}
