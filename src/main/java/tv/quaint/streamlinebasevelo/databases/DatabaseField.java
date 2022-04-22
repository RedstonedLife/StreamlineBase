package tv.quaint.streamlinebasevelo.databases;

public class DatabaseField<P> {
    public String key;
    public Object property;

    public DatabaseField(String key, P property) {
        this.key = key;
        this.property = property;
    }
}
