package Backend.HashTable.Event;

public class NodeVisitedEvent implements HashTableEvent {
    private final int key;

    public NodeVisitedEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
