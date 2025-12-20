package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.ChainingStrategy;
import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.LinkedList.LinkedList;
import Backend.HashTable.Event.HashTableEvent;
import java.util.function.Consumer;

public class SeparateChaining implements CollisionResolver {
    private final int tablesize;
    
    public SeparateChaining(int tablesize) {
        this.tablesize = tablesize;
    }
    
    public int getTableSize() {
        return this.tablesize;
    }
    
    @Override
    public int hash(int key) {
        return key % this.tablesize;
    }
    
    @Override
    public int probe(int key, int i) {
        return hash(key); 
    }
    
    @Override
    public String getName() {
        return "Separate Chaining";
    }
    
    @Override
    public HashTableStrategy createStrategy(
            LinkedList[] table, 
            int tableSize,
            Consumer<HashTableEvent> emit) {
        return new ChainingStrategy(table, this, emit);
    }
    
    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.CHAINING;
    }
    @Override
    public String getFormula() {
        return "No probing needed in Separate Chaining.";
    }
}
