package Backend.HashTable.Strategy;

import Backend.HashTable.Event.*;
import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.LinkedList.LinkedList;

import java.util.function.Consumer;

public class OpenAddressingStrategy implements HashTableStrategy {

    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private final int tableSize;
    private final Consumer<HashTableEvent> emit;

    public OpenAddressingStrategy(
            LinkedList[] table,
            CollisionResolver resolver,
            int tableSize,
            Consumer<HashTableEvent> emit
    ) {
        this.table = table;
        this.resolver = resolver;
        this.tableSize = tableSize;
        this.emit = emit;
    }

    @Override
    public boolean insert(int key, String value) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            emit.accept(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            
            // FIX BUG #1: Enforce single-node invariant for open addressing
            if (bucket.isEmpty()) {
                // Empty slot - insert here
                bucket.insert(key, value);
                return true;
            }
            
            // Bucket occupied - check if it's the same key (update case)
            String existingValue = bucket.search(key);
            if (existingValue != null) {
                // Same key exists - update it
                bucket.insert(key, value);
                return true;
            }
            
            // Bucket occupied by different key - collision, must probe next
            emit.accept(new CollisionBlockedEvent(index));
        }
        // Table is full
        return false;
    }

    @Override
    public String search(int key) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            emit.accept(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            
            // FIX BUG #2: Don't terminate early on empty bucket
            // Must complete full probe sequence
            if (bucket.isEmpty()) {
                continue; // Keep probing
            }
            
            String value = bucket.search(key);
            if (value != null) {
                return value; // Found it
            }
            // Not found in this bucket, continue probing
        }
        // Completed full probe cycle without finding key
        return null;
    }

    @Override
    public boolean delete(int key) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            emit.accept(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            
            // FIX BUG #2: Don't terminate early on empty bucket
            // Must complete full probe sequence
            if (bucket.isEmpty()) {
                continue; // Keep probing
            }
            
            if (bucket.delete(key)) {
                return true; // Successfully deleted
            }
            // Not found in this bucket, continue probing
        }
        // Completed full probe cycle without finding key
        return false;
    }
}