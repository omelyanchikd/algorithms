/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        String value = "";

        while (!StdIn.isEmpty()) {
            value = StdIn.readString();
            queue.enqueue(value);
        }
        for (int i = 0; i < k; i++) {
            value = queue.dequeue();
            StdOut.println(value);
        }


    }
}
