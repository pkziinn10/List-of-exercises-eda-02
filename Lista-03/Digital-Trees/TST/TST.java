import java.util.LinkedList;
import java.util.Queue;

public class TST<Value> implements ST<String, Value> {
    private int n; // tamanho
    private Node<Value> root;

    private static class Node<Value> {
        private char c;
        private Node<Value> left, mid, right;
        private Value val;
    }

    public TST() {
    }

    public int size() {
        return n;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("A chave para contains() não pode ser nula");
        return get(key) != null;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Value get(String key) {
        if (key == null) throw new IllegalArgumentException("A chamada get() tem argumento nulo");
        if (key.length() == 0) throw new IllegalArgumentException("A chave deve ter tamanho >= 1");
        Node<Value> x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node<Value> get(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) throw new IllegalArgumentException("A chave deve ter tamanho >= 1");
        char c = key.charAt(d);
        if      (c < x.c)              return get(x.left,  key, d);
        else if (c > x.c)              return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid,   key, d+1);
        else                           return x;
    }

    public void put(String key, Value val) {
        if (key == null) throw new IllegalArgumentException("A chave em put() não pode ser nula");
        if (val == null) {
            delete(key);
            return;
        }
        if (!contains(key)) n++;
        root = put(root, key, val, 0);
    }

    private Node<Value> put(Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node<Value>();
            x.c = c;
        }
        if      (c < x.c)               x.left  = put(x.left,  key, val, d);
        else if (c > x.c)               x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1)  x.mid   = put(x.mid,   key, val, d+1);
        else                            x.val   = val;
        return x;
    }

    public String longestPrefixOf(String query) {
        if (query == null) throw new IllegalArgumentException("O argumento em longestPrefixOf() não pode ser nulo");
        if (query.length() == 0) return null;
        int length = 0;
        Node<Value> x = root;
        int i = 0;
        while (x != null && i < query.length()) {
            char c = query.charAt(i);
            if      (c < x.c) x = x.left;
            else if (c > x.c) x = x.right;
            else {
                i++;
                if (x.val != null) length = i;
                x = x.mid;
            }
        }
        return query.substring(0, length);
    }

    public Iterable<String> keys() {
        Queue<String> queue = new LinkedList<String>();
        collect(root, new StringBuilder(), queue);
        return queue;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("O prefixo não pode ser nulo");
        Queue<String> queue = new LinkedList<String>();
        Node<Value> x = get(root, prefix, 0);
        if (x == null) return queue;
        if (x.val != null) queue.add(prefix);
        collect(x.mid, new StringBuilder(prefix), queue);
        return queue;
    }

    private void collect(Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x == null) return;
        collect(x.left,  prefix, queue);
        if (x.val != null) queue.add(prefix.toString() + x.c);
        collect(x.mid,   prefix.append(x.c), queue);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, queue);
    }
    
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("A chave em delete() não pode ser nula");
        if (contains(key)) n--;
        root = delete(root, key, 0);
    }

    private Node<Value> delete(Node<Value> x, String key, int d) {
        if (x == null) return null;
        if (key.length() == 0) return null;

        char c = key.charAt(d);
        if      (c < x.c)              x.left  = delete(x.left,  key, d);
        else if (c > x.c)              x.right = delete(x.right, key, d);
        else if (d < key.length() - 1) x.mid   = delete(x.mid,   key, d+1);
        else                           x.val   = null;

        if (x.val != null || x.left != null || x.mid != null || x.right != null) {
            return x;
        }
        return null;
    }
}
