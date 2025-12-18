package Backend.HashTable.Event;

public class NodeDeletedEvent implements HashTableEvent {
    private final int key;

    public NodeDeletedEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
