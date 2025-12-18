package Backend.HashTable.CollisionResolver;

public class LinearProbing implements CollisionResolver {
    private final int tablesize;//Not consider dynamic size change
    //Constructor
    public LinearProbing(int tablesize){
        this.tablesize = tablesize; 
    }
    //Getter
    public int getTableSize(){
        return this.tablesize;
    }
    //Override methods: index = (hash(key) + i) % tableSize
    @Override
    public int hash(int key){
        return key % this.tablesize;
    }
    @Override
    public int probe(int key,int i){
        return (hash(key) + i) % this.tablesize;
    }
    @Override
    public String getName(){
        return "Linear Probing";
    }
    @Override
    public boolean needsProbing(boolean empty) {
        return true && !empty;
    }
} 

