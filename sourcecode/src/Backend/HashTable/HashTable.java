package Backend.HashTable;

import Backend.HashTable.CollisionResolver.*;
import Backend.HashTable.Event.HashTableEvent;
import Backend.HashTable.Strategy.*;
import Backend.LinkedList.LinkedList;

import java.util.function.Consumer;

public class HashTable {

    private final int tableSize;
    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private HashTableStrategy strategy;
    private Consumer<HashTableEvent> eventSink = e -> {};

    public HashTable(int tableSize, CollisionResolver resolver) {
        this.tableSize = tableSize;
        this.resolver = resolver;
        this.table = new LinkedList[tableSize];

        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList();
            table[i].setEventSink(this::emit);
        }

        // Let the resolver create its own strategy
        this.strategy = resolver.createStrategy(table, tableSize, this::emit);
    }
    
    //Event Sink
    public void setEventSink(Consumer<HashTableEvent> sink) {
        this.eventSink = sink;
        for (LinkedList list : table) {
            list.setEventSink(this::emit);
        }
    }
    
    private void emit(HashTableEvent event) {
        eventSink.accept(event);
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

    public LinkedList getTableAtIndex(int index) {
        if (index >= 0 && index < tableSize) return table[index];
        return null;
    }

    public int getTableSize() {
        return tableSize;
    }
}