package tv.quaint.streamlinebase.utils.obj;

public class SingleSet<K, V> {
    public K key;
    public V value;

    public SingleSet(K key, V value){
        this.key = key;
        this.value = value;
    }

    public SingleSet<K, V> updateKey(K key){
        this.key = key;
        return this;
    }

    public SingleSet<K, V> updateValue(V value){
        this.value = value;
        return this;
    }
}
