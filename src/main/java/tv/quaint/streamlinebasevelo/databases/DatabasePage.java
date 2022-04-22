package tv.quaint.streamlinebasevelo.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class DatabasePage {
    public DatabaseConfiguration configuration;
    public String collectionName;
    public DatabaseField<?> key;
    public List<DatabaseField<?>> fields;

    public DatabasePage(DatabaseConfiguration configuration, String collectionName, DatabaseField<?> key) {
        this.configuration = configuration;
        this.collectionName = collectionName;
        this.key = key;
        this.fields = new ArrayList<>();
    }

    public void create() {
        Document set = new Document();

        for (DatabaseField<?> field : this.fields) {
            set.append(field.key, field.property);
        }

        if (configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoHandler.getCollection(this.configuration, this.collectionName).insertOne(set);
        }
    }

    public void update() {
        Document query = new Document();
        Document set = new Document();

        query.put(this.key.key, this.key.property);
        for (DatabaseField<?> field : this.fields) {
            set.append(field.key, field.property);
        }

        if (this.configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoHandler.getCollection(this.configuration, this.collectionName).updateOne(query, MongoHandler.getSetExecution(set));
        }
    }

    public DatabasePage get() {
        if (this.configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoCollection<Document> collection = MongoHandler.getCollection(this.configuration, this.collectionName);
            BasicDBObject query = new BasicDBObject();
            query.put(this.key.key, this.key.property);
            try {
                if (collection.find(query).first() == null) {
                    create();
                }
            } catch (Exception e) {
                e.printStackTrace();
                create();
            }
            Document document = collection.find(query).first();
            if (document == null) return this;

            for (DatabaseField<?> field : this.fields) {
                field.property = document.get(field.key, field.property);
            }
        }

        return this;
    }
}
