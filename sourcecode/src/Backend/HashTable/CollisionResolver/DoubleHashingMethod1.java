package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.HashTable.Strategy.OpenAddressingStrategy;
import Backend.LinkedList.LinkedList;
import Backend.HashTable.Event.HashTableListener;

public class DoubleHashingMethod1 implements CollisionResolver {
    private final int tablesize;

    public DoubleHashingMethod1(int tablesize) {
        this.tablesize = tablesize; 
    }
    
    public int getTableSize() {
        return this.tablesize;
    }

    public int hash2(int key) {
        return 1 + (key % (this.tablesize - 1));
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
        return "Double Hashing Method 1";
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
        return "h(k, i) = (h(k) + i × h₂(k)) mod " + tablesize
         + "\nwhere h₂(k) = 1 + (k mod (" + tablesize + " - 1))";
    }
}