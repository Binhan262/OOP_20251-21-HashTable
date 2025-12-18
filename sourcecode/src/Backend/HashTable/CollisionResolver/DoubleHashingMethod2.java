package Backend.HashTable.CollisionResolver;

public class DoubleHashingMethod2 implements CollisionResolver {
    private final int tablesize;//Not consider dynamic size change
    private final int prime;

    //Constructor
    public DoubleHashingMethod2(int tablesize, int prime){
        this.tablesize = tablesize; 
        this.prime = prime;
    }
    //Getter
    public int getTableSize(){
        return this.tablesize;
    }

    public int hash2(int key){
        return prime - (key % prime);
    }
    //Override methods: index = (hash(key) + i*hash2(key)) % tableSize
    @Override
    public int hash(int key){
        return key % this.tablesize;
    }
    @Override   
    public int probe(int key,int i){
        return (hash(key) + i * hash2(key)) % this.tablesize;
    }
    @Override
    public String getName(){
        return "Double Hashing Method 2";
    }

    @Override
    public boolean needsProbing(boolean empty) {
        return true && !empty;
    }
}
