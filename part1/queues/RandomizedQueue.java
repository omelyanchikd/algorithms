/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int N = 0;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "You cannot add null element to the randomized queue!");
        }
        if (N == queue.length) {
            resize(2 * N);
        }
        queue[N++] = item;
    }

    // resize array
    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    // swap elements
    private void swap(int i, int j) {
        Item value = queue[i];
        queue[i] = queue[j];
        queue[j] = value;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Your randomized queue is empty!");
        }
        if (N > 1) {
            int index = StdRandom.uniformInt(N);
            if (index != N - 1) {
                swap(index, N - 1);
            }
        }
        Item value = queue[--N];
        queue[N] = null;
        if (N > 0 && N == queue.length / 4) {
            resize(queue.length / 2);
        }
        return value;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Your randomized queue is empty!");
        }
        int index = StdRandom.uniformInt(N);
        return queue[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int current = N;

        public boolean hasNext() {
            return (current > 0);
        }

        public void remove() {
            throw new UnsupportedOperationException(
                    "Method remove is not supported for ArrayIterator");
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no elements in the deque!");
            }
            if (current > 1) {
                int index = StdRandom.uniformInt(current);
                if (index != current - 1) {
                    swap(index, current - 1);
                }
            }
            Item value = queue[--current];
            return value;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int trials = 12000;
        String value = "";
        for (int i = 0; i < trials; i++) {
            RandomizedQueue<String> queue = new RandomizedQueue<String>();
            queue.enqueue("AB");
            queue.enqueue("BA");
            for (String s : queue) {
                StdOut.println(s);
            }
        }
    }
}
