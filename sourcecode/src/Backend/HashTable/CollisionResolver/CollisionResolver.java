package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.LinkedList.LinkedList;
import java.util.function.Consumer;
import Backend.HashTable.Event.HashTableEvent;

public interface CollisionResolver {
    //Calculate initial hash index 
    int hash(int key);

    //Calculate next probe index at probe number i
    int probe(int key, int i);
    
    //Get name of collision 
    String getName();

    //Create appropriate strategy for this collision resolver
    HashTableStrategy createStrategy(
        LinkedList[] table, 
        int tableSize,
        Consumer<HashTableEvent> emit
    );
}