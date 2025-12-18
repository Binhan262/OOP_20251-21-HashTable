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
        // Compute ONE index, no probing
        int index = resolver.hash(key);
        //Access the bucket
        emit.accept(new BucketAccessedEvent(index));
        if (!table[index].isEmpty()) {
            emit.accept(new CollisionDetectedEvent(index));
        }

        table[index].insert(key, value);
        return true;
    }

    @Override
    public String search(int key) {
        // Compute ONE index, no probing
        int index = resolver.hash(key);
        //Access the bucket
        emit.accept(new BucketAccessedEvent(index));
        return table[index].search(key);
    }

    @Override
    public boolean delete(int key) {
        // Compute ONE index, no probing
        int index = resolver.hash(key);
        //Access the bucket
        emit.accept(new BucketAccessedEvent(index));
        return table[index].delete(key);
    }
}
