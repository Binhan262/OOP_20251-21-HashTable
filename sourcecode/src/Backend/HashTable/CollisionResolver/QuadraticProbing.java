package Backend.HashTable.CollisionResolver;

public class QuadraticProbing implements CollisionResolver{
    private final int tablesize;//Not consider dynamic size change
    //Constructor
    public QuadraticProbing(int tablesize){
        this.tablesize = tablesize; 
    }
    //Getter
    public int getTableSize(){
        return this.tablesize;
    }
    //Override methods: index = (hash(key) + i^2) % tableSize
    @Override
    public int hash(int key){
        return key % this.tablesize;
    }
    @Override
    public int probe(int key,int i){
        return (hash(key) + i*i) % this.tablesize;
    }
    @Override
    public String getName(){
        return "Quadratic Probing";
    }

    @Override
    public boolean needsProbing(boolean empty) {
        return true && !empty;
    }
}