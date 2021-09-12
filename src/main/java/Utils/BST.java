package Utils;
import java.util.NoSuchElementException;

@SuppressWarnings("Duplicates")
class BST<Key extends Comparable<Key>, Value> {
    private Node root;

    class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int count;

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key = " + key +
                    ", val = " + val +
                    ", left = " + left +
                    ", right = " + right +
                    '}';
        }
    }

    public void put(Key key, Value val) {
        root = put(root, key, val);
        System.out.println("ROOT:" + root.toString());
    }

    private Node put(Node x, Key key, Value val) {

        if (x == null) {
            System.out.println("NULL");
            return new Node(key, val);
        }
        System.out.println(x.toString());
        int cmp = key.compareTo(x.key);
        System.out.println("cmp: " + cmp);

        if (cmp < 0) {
            System.out.println("NEW PUT <");
            x.left = put(x.left, key, val);
        } else if (cmp > 0) {
            x.right = put(x.right, key, val);
            System.out.println("NEW PUT >");
        } else x.val = val;
        //TODO
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }


    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0)
            return x;
        if (cmp < 0)
            return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null)
            return t;
        return x;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }


    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    public Iterable<Key> keys() {
        Queue<Key> q = new Queue<>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node x, Queue<Key> q) {
        if (x == null) return;
        inorder(x.left, q);
        q.enqueue(x.key);
        inorder(x.right, q);
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {

        if (x == null) {
            System.out.println("NULL");
            return null;
        }

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            System.out.println("LEFT");
            x.left = delete(x.left, key);
        } else if (cmp > 0) {
            System.out.println("RIGHT");
            x.right = delete(x.right, key);
        } else {
            System.out.println("FOUND");
            System.out.println(x.toString());

            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;

            System.out.println("node T:" + t.toString());
            x = min(t.right);
            System.out.println("x = min " + x.toString());
            x.right = deleteMin(t.right);
            x.left = t.left;
            System.out.println("x rl" + x.toString());
        }
        x.count = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<>();
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    public static void main(String[] args) {
        BST<Integer, String> bst = new BST<>();
//        for (int i = 0; i < 7; i++) {
//            System.out.println((char) ('a' + i));
//            bst.put(i, (char) ('a' + i));
//            System.out.println(bst.keys[i]);
//        }
        bst.put(5, "d");
        System.out.println("--------------");
        bst.put(3, "q");
        System.out.println("--------------");
        bst.put(1, "z");
        System.out.println("--------------");
        bst.put(2, "e");
        System.out.println("--------------");
        bst.put(6, "l");
        System.out.println("--------------");
        bst.put(8, "m");
        System.out.println("--------------");
        bst.put(7, "n");
        System.out.println("--------------");
        bst.put(9, "r");
        System.out.println("--------------");

        for (Integer k : bst.keys()) System.out.print(bst.get(k) + " ");


        System.out.println("\n" + bst.max() + " " + bst.get(bst.max()));
        System.out.println("___________________________________________________");
        bst.delete(6);

        System.out.println(bst.root.toString());
        for (Integer k : bst.keys()) {
            System.out.print(bst.get(k) + " ");
        }

    }
}
