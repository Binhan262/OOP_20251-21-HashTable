package Backend.HashTable.CollisionResolver;
public class SeparateChaining implements CollisionResolver{
    private final int tablesize;//Not consider dynamic size change
    //Constructor
    public SeparateChaining(int tablesize){
        this.tablesize = tablesize;
    }
    //Getter
    public int getTableSize(){
        return this.tablesize;
    }
    //Override methods
    @Override
    public int hash(int key){
        return key % this.tablesize;
    }
    @Override
    public int probe(int key,int i){
        return hash(key); 
    }
    @Override
    public String getName(){
        return "Separate Chaining";
    }
    @Override
    public boolean needsProbing(boolean empty) {
        return false;
    }
}
