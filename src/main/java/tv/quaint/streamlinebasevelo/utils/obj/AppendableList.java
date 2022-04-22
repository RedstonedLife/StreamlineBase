package tv.quaint.streamlinebasevelo.utils.obj;


import java.util.ArrayList;
import java.util.List;

public class AppendableList<T> {
    public List<T> list;

    public AppendableList() {
        list = new ArrayList<>();
    }

    public AppendableList<T> append(T thing) {
        list.add(thing);

        return this;
    }
}
