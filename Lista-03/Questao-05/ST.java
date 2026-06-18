import java.util.LinkedList;
import java.util.Queue;

// Interface ST (Symbol Table)
interface ST<Key, Value> {
    void put(Key key, Value val);
    Value get(Key key);
    void delete(Key key);
    boolean contains(Key key);
    boolean isEmpty();
    int size();
    Iterable<Key> keys();
}