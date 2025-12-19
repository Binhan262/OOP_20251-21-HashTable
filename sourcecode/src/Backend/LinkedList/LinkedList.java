package Backend.LinkedList;

import java.util.function.Consumer;
import java.util.function.BiConsumer;
import Backend.HashTable.Event.HashTableEvent;
import Backend.HashTable.Event.NodeDeletedEvent;
import Backend.HashTable.Event.NodeInsertEvent;
import Backend.HashTable.Event.NodeVisitedEvent;

public class LinkedList {
    private Node head;
    
    public LinkedList() {
        this.head = null;
    }

    private Consumer<HashTableEvent> hashTableSink = e -> {};

    public void setHashTableSink(Consumer<HashTableEvent> sink) {
        if (sink != null) {
            this.hashTableSink = sink;
        }
    }

    private void emit(HashTableEvent event) {
        hashTableSink.accept(event);
    }

    // FIX: Better encapsulation - still expose head for now but add iterator
    public Node getHead() {
        return head;
    }
    
    // FIX: Add proper iteration method (better OOP)
    public void forEach(BiConsumer<Integer, String> visitor) {
        Node current = head;
        while (current != null) {
            visitor.accept(current.key, current.value);
            current = current.next;
        }
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
    
    public String search(int key) {
        Node cur = head;
        while (cur != null) {
            emit(new NodeVisitedEvent(cur.getKey()));
            if (cur.key == key) {
                return cur.value;
            }
            cur = cur.next;
        }
        return null;
    }
    
    public void insert(int key, String value) {
        if (head == null) {
            head = new Node(key, value);
            emit(new NodeInsertEvent(key, value));
            return;
        }
        
        Node cur = head;
        while (true) {
            emit(new NodeVisitedEvent(cur.getKey()));
            if (cur.key == key) {
                cur.value = value;
                return;
            }
            if (cur.next == null) {
                cur.next = new Node(key, value);
                emit(new NodeInsertEvent(key, value));
                return;
            }
            cur = cur.next;
        }
    }
    
    public boolean delete(int key) {
        if (head == null) {
            return false;
        }
        
        if (head.key == key) {
            emit(new NodeVisitedEvent(head.getKey()));
            head = head.next;
            emit(new NodeDeletedEvent(key));
            return true;
        }
        
        Node cur = head;
        emit(new NodeVisitedEvent(head.getKey()));
        while (cur.next != null) {
            if (cur.next.key == key) {
                cur.next = cur.next.next;
                emit(new NodeDeletedEvent(key));
                return true;
            }
            cur = cur.next;
            if (cur != null) {
                emit(new NodeVisitedEvent(cur.getKey()));
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
}