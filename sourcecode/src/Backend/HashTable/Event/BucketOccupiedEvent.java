package Backend.HashTable.Event;

public class BucketOccupiedEvent extends HashTableEvent {
    private final int index;
    
    public BucketOccupiedEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}