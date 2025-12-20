package Backend.HashTable;

import Backend.HashTable.CollisionResolver.*;
import Backend.HashTable.Event.HashTableEvent;
import Backend.HashTable.Event.HashTableListener;
import Backend.HashTable.Strategy.*;
import Backend.LinkedList.LinkedList;

public class HashTable {

    private final int tableSize;
    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private HashTableStrategy strategy;
    private HashTableListener eventListener = e -> {};

    public HashTable(int tableSize, CollisionResolver resolver) {
        this.tableSize = tableSize;
        this.resolver = resolver;
        this.table = new LinkedList[tableSize];

        // Initialize all lists
        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList();
        }

        this.strategy = resolver.createStrategy(table, tableSize, this::emit);
    }
    
    public void setEventListener(HashTableListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Event listener cannot be null");
        }
        this.eventListener = listener;
    }

    public void emit(HashTableEvent event) {
        if (eventListener != null) {
            eventListener.onEvent(event);
        }
    }

    public boolean insert(int key, String value) {
        return strategy.insert(key, value);
    }

    public String search(int key) {
        return strategy.search(key);
    }

    public boolean delete(int key) {
        return strategy.delete(key);
    }
    
    // Getters for visualization (encapsulated access)
    public int getTableSize() {
        return tableSize;
    }
    
    public LinkedList getTableAtIndex(int index) {
        if (index >= 0 && index < tableSize) {
            return table[index];
        }
        return null;
    }
    
    public CollisionResolver getResolver() {
        return resolver;
    }
}
