package Backend.HashTable.Strategy;

import Backend.HashTable.Event.*;
import Backend.HashTable.CollisionResolver.CollisionResolver;
import Backend.LinkedList.LinkedList;


public class ChainingStrategy implements HashTableStrategy {

    private final LinkedList[] table;
    private final CollisionResolver resolver;
    private final HashTableListener listener;

    public ChainingStrategy(
        LinkedList[] table,
        CollisionResolver resolver,
        HashTableListener listener
    ) {
        this.table = table;
        this.resolver = resolver;
        this.listener = listener;
    }

    @Override
    public boolean insert(int key, String value) {
        int index = resolver.hash(key);
        listener.onEvent(new BucketAccessedEvent(index));
        table[index].insert(key, value);
        listener.onEvent(new BucketAcceptedEvent(index));
        return true; // Always true for chaining - never full
    }

    @Override
    public String search(int key) {
        int index = resolver.hash(key);
        listener.onEvent(new BucketAccessedEvent(index));
        String result = table[index].search(key);
        if(result != null) {
            listener.onEvent(new BucketAcceptedEvent(index));
        }
        else {
            listener.onEvent(new CollisionBlockedEvent(index));
        }
        return result;
    }  

    @Override
    public boolean delete(int key) {
        int index = resolver.hash(key);
        listener.onEvent(new BucketAccessedEvent(index));
        boolean removed = table[index].delete(key);
        if(removed) {
            listener.onEvent(new BucketAcceptedEvent(index));
        }
        else {
            listener.onEvent(new CollisionBlockedEvent(index));
        }
        return removed;
    }
}