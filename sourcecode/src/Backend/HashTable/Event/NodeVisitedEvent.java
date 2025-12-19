package Backend.HashTable.Event;

public class NodeVisitedEvent extends HashTableEvent {
    private final int key;
    
    public NodeVisitedEvent(int key) {
        this.key = key;
    }
    
    public int getKey() {
        return key;
    }
}