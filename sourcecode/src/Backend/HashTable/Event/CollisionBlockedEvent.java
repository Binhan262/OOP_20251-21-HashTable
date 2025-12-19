package Backend.HashTable.Event;

public class CollisionBlockedEvent extends HashTableEvent {
    private final int index;
    
    public CollisionBlockedEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}