package Backend.HashTable.Event;

// Node deletion
public class NodeDeletedEvent extends HashTableEvent {
    private final int key;
    
    public NodeDeletedEvent(int key) {
        this.key = key;
    }
    
    public int getKey() {
        return key;
    }
}