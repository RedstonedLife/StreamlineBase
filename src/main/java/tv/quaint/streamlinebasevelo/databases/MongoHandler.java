package tv.quaint.streamlinebasevelo.databases;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoNamespace;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.concurrent.ConcurrentHashMap;

public class MongoHandler {
    public static ConcurrentHashMap<DatabaseConfiguration, MongoDatabase> loadedDatabase = new ConcurrentHashMap<>();

    public static void putDatabase(DatabaseConfiguration configuration, MongoDatabase database) {
        loadedDatabase.put(configuration, database);
    }

    public static void removeDatabase(DatabaseConfiguration configuration) {
        loadedDatabase.remove(configuration);
    }

    public static MongoDatabase getDatabase(DatabaseConfiguration configuration) {
        MongoDatabase database = loadedDatabase.get(configuration);
        if (database == null) {
            loadDatabase(configuration);
        }
        database = loadedDatabase.get(configuration);
        return database;
    }

    public static void loadDatabase(DatabaseConfiguration configuration) {
        if (getDatabase(configuration) != null) removeDatabase(configuration);

        MongoClientURI uri = new MongoClientURI(configuration.getUri());
        MongoClient client = new MongoClient(uri);

        MongoDatabase db = client.getDatabase(configuration.database);

        putDatabase(configuration, db);
    }

    public static MongoCollection<Document> getCollection(DatabaseConfiguration configuration, String collectionName) {
        return getDatabase(configuration).getCollection(collectionName);
    }

    public static Document getSetExecution(Document setData) {
        return new Document().append("$set", setData);
    }
}
