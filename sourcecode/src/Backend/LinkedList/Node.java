package Backend.LinkedList;

public class Node {
    public int key;
    public String value;
    public Node next;
    //Constructor
    public Node(int key, String value){
        this.key = key;
        this.value = value;
        this.next = null;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
