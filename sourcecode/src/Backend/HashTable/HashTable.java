package Backend.HashTable;
import java.util.function.Consumer;

import Backend.HashTable.CollisionResolver.*;
import Backend.HashTable.Event.BucketAccessedEvent;
import Backend.HashTable.Event.CollisionDetectedEvent;
import Backend.HashTable.Event.HashTableEvent;
import Backend.LinkedList.*;
public class HashTable {
    private int tablesize;
    private LinkedList[] table;
    private CollisionResolver collisionResolver;

    
    private Consumer<HashTableEvent> eventSink = e -> {};

    public void setEventSink(Consumer<HashTableEvent> sink) {
        this.eventSink = sink;
    }

    public void emit(HashTableEvent event) {
        eventSink.accept(event);
    }
    
    
    //Constructor
    public HashTable(int tablesize, CollisionResolver collisionResolver){
        this.tablesize = tablesize;
        this.collisionResolver = collisionResolver;
        table = new LinkedList[tablesize];
        for(int i=0;i<tablesize;i++){
            table[i]=new LinkedList();
        }
    }
    
    
    //Methods
    //Getter
    public int getTableSize(){
        return tablesize;
    }
    
    public LinkedList getTableAtIndex(int index) {
        if(index >= 0 && index < tablesize) {
            return table[index];
        }
        return null;
    }
    
    public CollisionResolver getCollisionResolver() {
        return collisionResolver;
    }
    //Insert
    public boolean insert(int key, String value){
        int step = 0;
        while(step < tablesize){
            int index = collisionResolver.probe(key, step);
            emit(new BucketAccessedEvent(index));
            if(!collisionResolver.needsProbing(table[index].isEmpty())){
                table[index].insert(key, value);
                return true; //Inserted successfully
            }
            emit(new CollisionDetectedEvent(index));
            step++;
        }
        return false; //No suitable slot found
    }
    //Search by key and return value 
    public String search(int key){
        int step = 0;
        while(step < tablesize){
            int index = collisionResolver.probe(key, step);
            emit(new BucketAccessedEvent(index));
            String value = table[index].search(key);
            if(value != null) return value;
            if(!collisionResolver.needsProbing(false)) return null;
            step++;
        }
        return null; //Not found
    }
    //Delete by key
    public boolean delete(int key){
        int step = 0;
        while(step < tablesize){
            int index = collisionResolver.probe(key, step);
            emit(new BucketAccessedEvent(index));
            boolean deleted = table[index].delete(key);
            if(deleted) return true; //Deleted successfully
            if(!collisionResolver.needsProbing(false)) return false;
            step++;
        }
        return false; //Not found
    }
}
