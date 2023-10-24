/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int size = 0;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot add null element to the deque");
        }
        Node oldfirst = first;
        first = new Node();
        first.previous = null;
        first.item = item;
        first.next = oldfirst;
        if (oldfirst != null) {
            oldfirst.previous = first;
        }
        else {
            last = first;
        }
        size += 1;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You cannot add null element to the deque");
        }
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (oldlast != null) {
            oldlast.next = last;
        }
        else {
            first = last;
        }
        last.previous = oldlast;
        size += 1;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty!");
        }
        Item value = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }
        else {
            first.previous = null;
        }
        size -= 1;
        return value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty!");
        }
        Item value = last.item;
        size -= 1;
        last = last.previous;
        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private Node node = first;

        public boolean hasNext() {
            return node != null;
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Method remove is not supported for ArrayIterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no elements in the deque!");
            }
            Item value = node.item;
            node = node.next;
            return value;
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        StdOut.println(deque.isEmpty());
        deque.addFirst("first");
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.size());
        deque.addLast("second");
        for (String s : deque) {
            StdOut.println(s);
        }
        StdOut.println(deque.size());
        String value = deque.removeFirst();
        StdOut.println(value);
        for (String s : deque) {
            StdOut.println(s);
        }
        value = deque.removeLast();
        StdOut.println(value);
        for (String s : deque) {
            StdOut.println(s);
        }
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.size());
    }

}
