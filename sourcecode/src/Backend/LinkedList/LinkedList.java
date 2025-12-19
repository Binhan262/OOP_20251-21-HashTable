package Backend.LinkedList;

import java.util.function.Consumer;

import Backend.HashTable.Event.HashTableEvent;
import Backend.HashTable.Event.NodeDeletedEvent;
import Backend.HashTable.Event.NodeInsertEvent;
import Backend.HashTable.Event.NodeVisitedEvent;

public class LinkedList {
    private Node head;
    //Constructor
    public LinkedList(){
        this.head = null;
    }

    private Consumer<HashTableEvent> eventSink = e -> {};

    public void setEventSink(Consumer<HashTableEvent> sink) {
        this.eventSink = sink;
    }

    private void emit(HashTableEvent event) {
        eventSink.accept(event);
    }

    //Getter
    public Node getHead() {
        return head;
    }
    public int getLength() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
    //Search
    public String search(int key) {
        Node cur=head;
        while(cur!=null){
            emit(new NodeVisitedEvent(cur.getKey()));
            if(cur.key==key){
                return cur.value;
            }
            cur=cur.next;
        }
        return null; // Key not found
    }
    //Insert(Check if exist same key first)
    public void insert(int key, String value) {
        if(head==null){
            head=new Node(key,value);
            emit(new NodeInsertEvent(key, value));
            return;
        }
        Node cur=head;
        while(true){
            emit(new NodeVisitedEvent(cur.getKey()));
            if(cur.key==key){
                cur.value=value; // Update value if key exists
                return;
            }
            if(cur.next==null){
                cur.next=new Node(key,value);
                emit(new NodeInsertEvent(key, value));
                return;
            }
        }
    }
    //Delete 
    public boolean delete(int key) {
        if(head==null){
            return false; // List is empty
        }
        if(head.key==key){
            emit(new NodeVisitedEvent(head.getKey()));
            head=head.next;
            emit(new NodeDeletedEvent(key));
            return true;
        }
        Node cur=head;
        emit(new NodeVisitedEvent(head.getKey()));
        while(cur.next!=null){
            if(cur.next.key==key){
                cur.next=cur.next.next;
                emit(new NodeDeletedEvent(key));
                return true;
            }
            cur=cur.next;
            emit(new NodeVisitedEvent(cur.getKey()));
        }
        return false; // Key not found
    }
    //Check if empty
    public boolean isEmpty() {
        return head == null;
    }
}
