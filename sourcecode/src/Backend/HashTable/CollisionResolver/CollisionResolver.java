package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.LinkedList.LinkedList;
import java.util.function.Consumer;
import Backend.HashTable.Event.HashTableEvent;

public interface CollisionResolver {
    int hash(int key);
    int probe(int key, int i);
    String getName();
    
    HashTableStrategy createStrategy(
        LinkedList[] table, 
        int tableSize,
        Consumer<HashTableEvent> emit
    );
    
    VisualizationType getVisualizationType();
    
    enum VisualizationType {
        CHAINING,
        OPEN_ADDRESSING
    }
}
