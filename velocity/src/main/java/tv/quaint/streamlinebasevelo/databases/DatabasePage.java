package tv.quaint.streamlinebasevelo.databases;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import tv.quaint.streamlinebasevelo.StreamlineBase;
import tv.quaint.streamlinebasevelo.utils.obj.NodeDocument;
import tv.quaint.streamlinebasevelo.utils.obj.SingleSet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DatabasePage {
    public boolean isCreating = false;
    public DatabaseConfiguration configuration;
    public String collectionName;
    public SingleSet<String, DatabaseThing<?>> queryKey;
    public List<SingleSet<String, DatabaseThing<?>>> fields = new ArrayList<>();

    public DatabasePage(DatabaseConfiguration configuration, String collectionName, SingleSet<String, DatabaseThing<?>> queryKey) {
        this.configuration = configuration;
        this.collectionName = collectionName;
        this.queryKey = queryKey;
        this.fields.add(queryKey);
    }

    public SingleSet<String, ?> getField(String key) {
        for (SingleSet<String, ?> field : this.fields) {
            if (field.key.equals(key)) return field;
        }
        return null;
    }

    public void addField(SingleSet<String, DatabaseThing<?>> field) {
        this.fields.add(field);
    }

    public void removeField(String key) {
        this.fields.remove(getField(key));
    }

    public void removeField(SingleSet<String, ?> field) {
        removeField(field.key);
    }

    public void flushFields() {
        this.fields = new ArrayList<>();
    }

    public void create() {
        isCreating = true;
        if (configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoHandler.getCollection(this.configuration, this.collectionName).insertOne(new NodeDocument(this.fields).parse());
        }
        if (configuration.of.equals(DatabaseConfiguration.SupportedType.LOCAL)) {

        }
        isCreating = false;
    }

    public void update() {
        if (this.configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoCollection<Document> collection = MongoHandler.getCollection(this.configuration, this.collectionName);
            BasicDBObject query = new BasicDBObject();
            query.put(this.queryKey.key, this.queryKey.value.thing);
            try {
                if (collection.find(query).first() == null) {
                    if (isCreating) {
                        StreamlineBase.SERVER.getScheduler().buildTask(StreamlineBase.INSTANCE, this::create).delay(5, TimeUnit.SECONDS).schedule();
                    } else {
                        create();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isCreating) {
                    StreamlineBase.SERVER.getScheduler().buildTask(StreamlineBase.INSTANCE, this::create).delay(5, TimeUnit.SECONDS).schedule();
                } else {
                    create();
                }
            }

            MongoHandler.getCollection(this.configuration, this.collectionName).updateOne(query, MongoHandler.getSetExecution(new NodeDocument(this.fields).parse()));
        }

        if (configuration.of.equals(DatabaseConfiguration.SupportedType.LOCAL)) {

        }
    }

    public DatabasePage get() {
        if (this.configuration.of.equals(DatabaseConfiguration.SupportedType.MONGO)) {
            MongoCollection<Document> collection = MongoHandler.getCollection(this.configuration, this.collectionName);
            BasicDBObject query = new BasicDBObject();
            query.put(this.queryKey.key, this.queryKey.value.thing);
            try {
                if (collection.find(query).first() == null) {
                    if (isCreating) {
                        StreamlineBase.SERVER.getScheduler().buildTask(StreamlineBase.INSTANCE, this::create).delay(5, TimeUnit.SECONDS).schedule();
                    } else {
                        create();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isCreating) {
                    StreamlineBase.SERVER.getScheduler().buildTask(StreamlineBase.INSTANCE, this::create).delay(5, TimeUnit.SECONDS).schedule();
                } else {
                    create();
                }
            }
            Document document = collection.find(query).first();
            if (document == null) return this;

            for (SingleSet<String, DatabaseThing<?>> field : new ArrayList<>(this.fields)) {
                this.fields.remove(field);

                this.fields.add(new SingleSet<>(field.key, new DatabaseThing<>(document.get(field.key, field.value.thing))));
            }
        }

        return this;
    }
}
