package tv.quaint.streamlinebasevelo.utils.obj;

import org.bson.Document;
import tv.quaint.streamlinebasevelo.databases.DatabaseThing;

import java.util.List;

public class NodeDocument {
    public Node root = new Node("root", new DatabaseThing<>(null));

    public NodeDocument(List<SingleSet<String, DatabaseThing<?>>> fields) {
        for (SingleSet<String, DatabaseThing<?>> f : fields) {
            root.append(f.key, f.value);
        }
    }

    public Document parse() {
        Document master = new Document();
        for (Node node : root.children) {
            if (node.thing == null) {
                master.put(node.self, parseFrom(node));
            } else {
                master.put(node.self, node.thing.thing);
            }
        }

        return master;
    }

    public Document parseFrom(Node node) {
        Document current = new Document();

        for (Node n : node.children) {
            if (n.thing == null) {
                current.put(n.self, parseFrom(n));
            } else {
                current.put(n.self, n.thing.thing);
            }
        }

        return current;
    }
}
