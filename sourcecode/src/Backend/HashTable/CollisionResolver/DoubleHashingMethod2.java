package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.HashTable.Strategy.OpenAddressingStrategy;
import Backend.LinkedList.LinkedList;
import Backend.HashTable.Event.HashTableEvent;
import java.util.function.Consumer;

public class DoubleHashingMethod2 implements CollisionResolver {
    private final int tablesize;
    private final int prime;

    public DoubleHashingMethod2(int tablesize, int prime) {
        this.tablesize = tablesize; 
        this.prime = prime;
    }
    
    public int getTableSize() {
        return this.tablesize;
    }

    public int hash2(int key) {
        return prime - (key % prime);
    }
    
    @Override
    public int hash(int key) {
        return key % this.tablesize;
    }
    
    @Override   
    public int probe(int key, int i) {
        return (hash(key) + i * hash2(key)) % this.tablesize;
    }
    
    @Override
    public String getName() {
        return "Double Hashing Method 2";
    }
    
    @Override
    public HashTableStrategy createStrategy(
            LinkedList[] table, 
            int tableSize,
            Consumer<HashTableEvent> emit) {
        return new OpenAddressingStrategy(table, this, tableSize, emit);
    }
    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.OPEN_ADDRESSING;
    }
}