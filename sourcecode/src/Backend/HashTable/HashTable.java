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

        // Initialize all lists
        for (int i = 0; i < tableSize; i++) {
            table[i] = new LinkedList();
            // 🔥 CRITICAL FIX: LinkedList forwards events through HashTable
            table[i].setHashTableSink(this::emit);
        }

        // Let the resolver create its own strategy (Factory Method Pattern)
        this.strategy = resolver.createStrategy(table, tableSize, this::emit);
    }
    
    // 🔥 CRITICAL FIX: HashTable is SINGLE SOURCE OF TRUTH for events
    public void setEventSink(Consumer<HashTableEvent> sink) {
        if (sink == null) {
            throw new IllegalArgumentException("Event sink cannot be null");
        }
        
        this.eventSink = sink;
        
        // NOTE: We do NOT call setHashTableSink() here again
        // Lists already have it set in constructor
    }
    
    // 🔥 CRITICAL: All events flow through this single emission point
    // LinkedLists forward here, then this forwards to AnimationManager
    public void emit(HashTableEvent event) {
        if (eventSink != null) {
            eventSink.accept(event);
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

/*
 * 🔥 CRITICAL ARCHITECTURE:
 * 
 * Event Flow (CORRECT):
 * LinkedList → HashTable.emit() → AnimationManager
 * 
 * Event Flow (WRONG - OLD CODE):
 * LinkedList → AnimationManager (causes interleaving!)
 * HashTable → AnimationManager (causes interleaving!)
 * 
 * RULE: Only ONE class emits to external world: HashTable
 * All internal components forward through HashTable.emit()
 * 
 * This fixes the JVM freeze bug on 3rd insert in chaining.
 */