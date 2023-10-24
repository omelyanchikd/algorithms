/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        int i = 1;
        while (true) {
            if (StdIn.isEmpty()) {
                StdOut.println(champion);
                break;
            }
            String word = StdIn.readString();
            boolean success = StdRandom.bernoulli(1.0 / i);
            if (success) {
                champion = word;
            }
            i += 1;
        }


    }
}

