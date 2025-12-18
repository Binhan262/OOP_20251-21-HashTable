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
        Node cur=head;
        emit(new NodeVisitedEvent(cur.getKey()));
        //Check if exist same key->Upgrade value
        while(cur!=null){
            if(cur.key==key) cur.value=value;           
            cur=cur.next;
            emit(new NodeVisitedEvent(cur.getKey()));
        }
        //Insert new node
        Node newNode=new Node(key,value);
        if(head==null){
            head=newNode;
            emit(new NodeInsertEvent(key, value));
        }
        else{
            cur=head;
            emit(new NodeVisitedEvent(cur.getKey()));
            while(cur.next!=null){
                cur=cur.next;
                emit(new NodeVisitedEvent(cur.getKey()));
            }
            cur.next=newNode;
            emit(new NodeInsertEvent(key, value));
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
