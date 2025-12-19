package Backend.HashTable.Event;

// Open Addressing: collision blocked (error, must probe)
public class CollisionBlockedEvent extends HashTableEvent {
    private final int index;
    
    public CollisionBlockedEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}