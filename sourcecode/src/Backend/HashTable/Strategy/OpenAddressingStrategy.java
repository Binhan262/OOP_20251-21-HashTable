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
            // If key already exists, update value
            if (bucket.search(key) != null) {
                bucket.insert(key, value);
                return true;
            }
            // Check if bucket is empty
            if (bucket.isEmpty()) {
                bucket.insert(key, value);
                return true;
            }
            // Bucket occupied by different key = collision
            emit.accept(new CollisionDetectedEvent(index));
        }
        return false;
    }

    @Override
    public String search(int key) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            emit.accept(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            // Try to find key in this bucket
            String value = bucket.search(key);
            if (value != null) return value;
            // If bucket is empty, key does not exist
            if (bucket.isEmpty()) return null;
        }
        return null;
    }

    @Override
    public boolean delete(int key) {
        for (int step = 0; step < tableSize; step++) {
            int index = resolver.probe(key, step);
            emit.accept(new BucketAccessedEvent(index));

            LinkedList bucket = table[index];
            // Try to find key in this bucket
            if (bucket.delete(key)) return true;
            // If bucket is empty, key does not exist
            if (bucket.isEmpty()) return false;
        }
        return false;
    }
}
