package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.LinkedList.LinkedList;

import Backend.HashTable.Event.HashTableListener;

public interface CollisionResolver {
    int hash(int key);
    int probe(int key, int i);
    String getName();
    String getFormula();
    HashTableStrategy createStrategy(
        LinkedList[] table, 
        int tableSize,
        HashTableListener listener
    );
    
    VisualizationType getVisualizationType();
    
    enum VisualizationType {
        CHAINING,
        OPEN_ADDRESSING
    }
}
