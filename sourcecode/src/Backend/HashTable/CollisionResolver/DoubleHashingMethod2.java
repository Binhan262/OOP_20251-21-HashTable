package Backend.HashTable.CollisionResolver;

import Backend.HashTable.Strategy.HashTableStrategy;
import Backend.HashTable.Strategy.OpenAddressingStrategy;
import Backend.LinkedList.LinkedList;
import Backend.HashTable.Event.HashTableListener;


public class DoubleHashingMethod2 implements CollisionResolver {
    private final int tablesize;
    private final int prime;

    public DoubleHashingMethod2(int tablesize) {
        this.tablesize = tablesize; 
        this.prime = previousPrime(tablesize);
    }
    
    public int getTableSize() {
        return this.tablesize;
    }
    //Find biggest prime smaller than tablesize
    private static boolean isPrime(int n) {
        if(n<=1) return false;
        if(n<=3) return true;
        if(n%2==0||n%3==0) return false;
        //Prime must be of form 6k +/- 1
        for (int i=5; i*i<= n; i+= 6) {
            if (n%i==0||n%(i+2)==0) return false;
        }
        return true;
    }
    private static int previousPrime(int n) {
        for (int i=n-1;i>=2;i--) {
            if (isPrime(i)) {
                return i;
            }
        }
        return 2; //Fallback
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
            HashTableListener listener) {
        return new OpenAddressingStrategy(table, this, tableSize, listener);
    }
    @Override
    public VisualizationType getVisualizationType() {
        return VisualizationType.OPEN_ADDRESSING;
    }
    @Override
    public String getFormula() {
        return "h(k, i) = (h(k) + i x h₂(k)) mod " + tablesize
         + "\nwhere h₂(k) = " + prime + " - (k mod " + prime + ")";
    }
}