package Backend.LinkedList;

import java.util.function.BiConsumer;

public class LinkedList {
    private Node head;
    
    public LinkedList() {
        this.head = null;
    }

    public Node getHead() {
        return head;
    }
    
    public void forEach(NodeVisitor visitor) {
        Node current = head;
        while (current != null) {
            visitor.visit(current.key, current.value);
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
            return;
        }
        
        Node cur = head;
        while (true) {
            if (cur.key == key) {
                cur.value = value;
                return;
            }
            if (cur.next == null) {
                cur.next = new Node(key, value);
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
            head = head.next;
            return true;
        }
        
        Node cur = head;
        while (cur.next != null) {
            if (cur.next.key == key) {
                cur.next = cur.next.next;
                return true;
            }
            cur = cur.next;
        }
        return false;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
}