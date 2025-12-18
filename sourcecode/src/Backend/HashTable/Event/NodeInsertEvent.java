package Backend.HashTable.Event;

public class NodeInsertEvent implements HashTableEvent {
    private final int key;
    private final String value;

    public NodeInsertEvent(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
