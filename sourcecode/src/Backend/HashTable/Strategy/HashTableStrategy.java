package Backend.HashTable.Strategy;

public interface HashTableStrategy {
    boolean insert(int key, String value);
    String search(int key);
    boolean delete(int key);
}
