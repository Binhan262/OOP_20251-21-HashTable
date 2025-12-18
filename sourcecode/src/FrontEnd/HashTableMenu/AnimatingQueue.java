package FrontEnd.HashTableMenu;

import java.util.ArrayDeque;
import java.util.Queue;

import Backend.HashTable.Event.HashTableEvent;

public class AnimatingQueue {
    private final Queue<HashTableEvent> queue = new ArrayDeque<>();

    public void add(HashTableEvent event) {
        queue.add(event);
    }

    public HashTableEvent poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
