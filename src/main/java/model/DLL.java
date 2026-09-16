package model;

public class DLL {
    private final int capacity;
    private final Node head;
    private final Node tail;
    private int size;

    public DLL(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public void insertAtMRU(Node node) {
        tail.prev.next = node;
        node.prev = tail.prev;
        node.next = tail;
        tail.prev = node;
        size++;
    }

    public Node removeLRU() {
        if(size == 0) return null;
        Node lru = head.next;
        removeNode(lru);
        return lru;
    }

    public void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public boolean isFull() {
        return size == capacity;
    }
}
