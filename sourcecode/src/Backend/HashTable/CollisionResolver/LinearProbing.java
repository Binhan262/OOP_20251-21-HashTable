package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.HashTable.Strategy.OpenAddressingStrategy;
import Backend.LinkedList.LinkedList;
import Backend.HashTable.Event.HashTableListener;


public class LinearProbing implements CollisionResolver {
    private final int tablesize;
    
    public LinearProbing(int tablesize) {
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
        return (hash(key) + i) % this.tablesize;
    }
    
    @Override
    public String getName() {
        return "Linear Probing";
    }
    
    @Override
    public HashTableStrategy createStrategy(
            LinkedList[] table, 
            int tableSize,
            HashTableListener listener) {
        return new OpenAddressingStrategy(table, this, tableSize, listener);
    }
    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.OPEN_ADDRESSING;
    }
    @Override
    public String getFormula() {
        return "h(k, i) = (h(k) + i) mod " + tablesize;   
    }
}