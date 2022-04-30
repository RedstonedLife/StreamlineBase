package tv.quaint.streamlinebase.databases;

import java.util.ArrayList;
import java.util.List;

public class Stringable {
    public Stringable appended = new Stringable("", "");
    public String self;
    public String parents;
    public List<String> children = new ArrayList<>();
    public int depth;

    public Stringable(String self) {
        this.self = self;
        this.depth = 0;
        this.parents = "";
    }

    public Stringable(String self, String parents) {
        this.self = self;
        this.depth = 0;
        this.parents = parents;
    }

    public Stringable(String self, String parents, int depth) {
        this.self = self;
        this.depth = depth;
        this.parents = parents;
    }

    public Stringable append(String append) {
        this.appended = new Stringable(append,  this.parents, this.depth + 1);
        this.children.add(append);

        return this.appended;
    }

    public boolean hasAppended() {
        return ! appended.self.equals("");
    }

    public boolean hasChildren() {
        return this.children.size() > 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(this.self);

        for (Stringable stringable = this; stringable.hasAppended(); stringable = stringable.appended) {
            stringable = stringable.appended;
            builder.append(stringable.self);
        }

        return builder.toString();
    }
}
