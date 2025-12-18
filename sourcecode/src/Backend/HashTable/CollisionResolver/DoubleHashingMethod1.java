package Backend.HashTable.CollisionResolver;

public class DoubleHashingMethod1 implements CollisionResolver {
    private final int tablesize;//Not consider dynamic size change

    //Constructor
    public DoubleHashingMethod1(int tablesize){
        this.tablesize = tablesize; 
    }
    //Getter
    public int getTableSize(){
        return this.tablesize;
    }

    public int hash2(int key){
        return 1 + (key % (this.tablesize - 1));
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
        return "Double Hashing Method 1";
    }
    @Override
    public boolean needsProbing(boolean empty) {
        return true && !empty;
    }
}
