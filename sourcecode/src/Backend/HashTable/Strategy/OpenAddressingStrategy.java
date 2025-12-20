package Backend.HashTable.Strategy;

import Backend.HashTable.Event.*;
import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.LinkedList.LinkedList;


public class OpenAddressingStrategy implements HashTableStrategy {

    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private final int tableSize;
    private final HashTableListener listener;

    public OpenAddressingStrategy(
        LinkedList[] table,
        CollisionResolver resolver,
        int tableSize,
        HashTableListener listener
    ) {
        this.table = table;
        this.resolver = resolver;
        this.tableSize = tableSize;
        this.listener = listener;
    }

    @Override
    public boolean insert(int key, String value) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            listener.onEvent(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            if (bucket.isEmpty()) {
                // Empty slot - insert here
                bucket.insert(key, value);
                listener.onEvent(new BucketAcceptedEvent(index));
                return true;
            }
            
            // Bucket occupied - check if it's the same key (update case)
            String existingValue = bucket.search(key);
            if (existingValue != null) {
                // Same key exists - update it
                bucket.insert(key, value);
                listener.onEvent(new BucketAcceptedEvent(index));
                return true;
            }
            // Bucket occupied by different key - collision, must probe next
            listener.onEvent(new CollisionBlockedEvent(index));
        }
        // Table is full
        return false;
    }

    @Override
    public String search(int key) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            listener.onEvent(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            if (bucket.isEmpty()) {
                listener.onEvent(new CollisionBlockedEvent(index));
                continue; // Keep probing
            }
            
            String value = bucket.search(key);
            if (value != null) {
                listener.onEvent(new BucketAcceptedEvent(index));
                return value; // Found it
            }
            else {
                listener.onEvent(new CollisionBlockedEvent(index));
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
            listener.onEvent(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            if (bucket.isEmpty()) {
                listener.onEvent(new CollisionBlockedEvent(index));
                continue; // Keep probing
            }
            
            if (bucket.delete(key)) {
                listener.onEvent(new BucketAcceptedEvent(index));
                return true; // Successfully deleted
            }
            else {
                listener.onEvent(new CollisionBlockedEvent(index));
            }
            // Not found in this bucket, continue probing
        }
        // Completed full probe cycle without finding key
        return false;
    }
}