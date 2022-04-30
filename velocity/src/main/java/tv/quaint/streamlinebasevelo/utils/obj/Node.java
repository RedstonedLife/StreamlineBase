package tv.quaint.streamlinebasevelo.utils.obj;

import tv.quaint.streamlinebasevelo.databases.DatabaseThing;
import tv.quaint.streamlinebasevelo.utils.BaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public String self;
    public List<Node> children = new ArrayList<>();
    public DatabaseThing<?> thing;

    public Node(String self, DatabaseThing<?> thing) {
        String[] split = self.split("\\.", 2);
        this.self = split[0];
        if (split.length > 1) {
            this.thing = null;
            this.append(split[1], thing);
        } else {
            this.thing = thing;
        }
    }

    public void append(String string, DatabaseThing<?> thing) {
        String[] split = string.split("\\.", 2);
        if (split.length > 1) {
            Node node;
            if (containsNode(split[0])) {
                node = getChild(split[0]);

                node.append(split[1], thing);
            } else {
                node = new Node(string, thing);
            }

            children.add(node);
        } else {
            children.add(new Node(split[0], thing));
        }
    }

    public boolean containsNode(String string) {
        for (Node child : children) {
            if (child.self.equals(string)) return true;
        }

        return false;
    }

    public Node getChild(String string) {
        for (Node child : children) {
            if (child.self.equals(string)) return child;
        }

        return null;
    }

    @Override
    public String toString() {
        return this.self + " > " + (this.thing != null ? this.thing.thing : "null");
    }
}
