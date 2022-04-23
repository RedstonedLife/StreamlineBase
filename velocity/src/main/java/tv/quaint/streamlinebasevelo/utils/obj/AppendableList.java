package tv.quaint.streamlinebasevelo.utils.obj;


import java.util.ArrayList;
import java.util.List;

public class AppendableList<T> {
    public List<T> list;

    public AppendableList() {
        list = new ArrayList<>();
    }

    @SafeVarargs
    public AppendableList(T... type) {
        list = List.of(type);
    }

    public AppendableList(AppendableList<T> other, T add) {
        list = new ArrayList<>();
        list.addAll(other.list);
        list.add(add);
    }
    public AppendableList<T> append(T thing) {
        list.add(thing);

        return this;
    }
}
