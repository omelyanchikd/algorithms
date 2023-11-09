/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class RWayTrie<Value> {
    private static final int R = 26;
    private Node root = new Node();

    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }

    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.value = val;
            return x;
        }
        int c = key.charAt(d) - 'A';
        x.next[c] = put(x.next[c], key, val, d + 1);
        return x;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.value;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        int c = key.charAt(d) - 'A';
        return get(x.next[c], key, d + 1);
    }

    public boolean containsPrefix(String prefix) {
        Node x = root;
        int d = 0;
        while (x != null) {
            if (d == prefix.length()) return true;
            int c = prefix.charAt(d) - 'A';
            x = x.next[c];
            d += 1;
        }
        return false;
    }

    public static void main(String[] args) {
        RWayTrie<String> trie = new RWayTrie<String>();
        trie.put("ABC", "ABC");
        System.out.println(trie.containsPrefix("AB"));
        System.out.println(trie.containsPrefix("ABC"));
    }
}
