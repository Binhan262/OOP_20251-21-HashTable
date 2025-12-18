package Backend.HashTable.Event;
public class BucketAccessedEvent implements HashTableEvent {
    private final int index;

    public BucketAccessedEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

}