package Backend.HashTable.Event;

public class BucketAcceptedEvent extends HashTableEvent {
    private final int index;
    
    public BucketAcceptedEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}