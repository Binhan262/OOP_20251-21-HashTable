package Backend.HashTable.Event;

public class CollisionDetectedEvent implements HashTableEvent {
    private final int index;

    public CollisionDetectedEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
