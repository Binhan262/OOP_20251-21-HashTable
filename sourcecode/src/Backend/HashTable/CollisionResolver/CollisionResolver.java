package Backend.HashTable.CollisionResolver;

public interface CollisionResolver {
    //Calculate initial hash index 
    int hash (int key);

    //Caculate next probe index at probe number i
    int probe (int key, int i);
    
    //Get name of collision 
    String getName();

    //Checking whether collision resolver needs probing
    boolean needsProbing(boolean empty);
} 
