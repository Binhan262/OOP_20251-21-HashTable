package Backend.HashTable.Event;
// Bucket access (common to both strategies)
public class BucketAccessedEvent extends HashTableEvent {
    private final int index;
    
    public BucketAccessedEvent(int index) {
        this.index = index;
    }
    
    public int getIndex() {
        return index;
    }
}