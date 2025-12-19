package Backend.HashTable.Strategy;

import Backend.HashTable.Event.*;
import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.LinkedList.LinkedList;

import java.util.function.Consumer;

public class ChainingStrategy implements HashTableStrategy {

    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private final Consumer<HashTableEvent> emit;

    public ChainingStrategy(
            LinkedList[] table,
            CollisionResolver resolver,
            Consumer<HashTableEvent> emit
    ) {
        this.table = table;
        this.resolver = resolver;
        this.emit = emit;
    }

    @Override
    public boolean insert(int key, String value) {
        int index = resolver.hash(key);
        emit.accept(new BucketAccessedEvent(index));
        
        if (!table[index].isEmpty()) {
            emit.accept(new BucketOccupiedEvent(index));
        }

        table[index].insert(key, value);
        return true; // Always true for chaining - never full
    }

    @Override
    public String search(int key) {
        int index = resolver.hash(key);
        emit.accept(new BucketAccessedEvent(index));
        return table[index].search(key);
    }

    @Override
    public boolean delete(int key) {
        int index = resolver.hash(key);
        emit.accept(new BucketAccessedEvent(index));
        return table[index].delete(key);
    }
}