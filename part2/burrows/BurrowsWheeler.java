/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class BurrowsWheeler {
    private final static int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        while (!BinaryStdIn.isEmpty()) {
            String input = BinaryStdIn.readString();
            CircularSuffixArray csa = new CircularSuffixArray(input);
            int L = input.length();
            int first = -1;
            char[] burrowsWheeler = new char[L];
            for (int i = 0; i < L; i++) {
                int sortedIndex = csa.index(i);
                if (sortedIndex == 0) first = i;
                burrowsWheeler[i] = (input.charAt((sortedIndex + L - 1) % L));
            }
            BinaryStdOut.write(first);
            for (int i = 0; i < L; i++)
                BinaryStdOut.write(burrowsWheeler[i]);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        while (!BinaryStdIn.isEmpty()) {
            int first = BinaryStdIn.readInt();
            char[] t = BinaryStdIn.readString().toCharArray();
            char[] firstColumn = sortString(t);
            int[] next = computeNext(firstColumn, t);
            int N = t.length;
            char[] input = new char[N];
            for (int i = 0; i < N; i++) {
                input[i] = firstColumn[first];
                first = next[first];
            }
            BinaryStdOut.write(new String(input));
        }
        BinaryStdOut.close();

    }

    // sort suffixes
    private static char[] sortString(char[] a) {
        int N = a.length;
        int[] count = new int[R + 1];
        char[] aux = new char[N];
        for (int i = 0; i < N; i++)
            count[a[i] + 1]++;
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < N; i++)
            aux[count[a[i]]++] = a[i];
        return aux;
    }

    // deduce next[] from t and first
    private static int[] computeNext(char[] firstColumn, char[] t) {
        int N = t.length;
        ST<Character, ArrayList<Integer>> tIndexes = createST(t);
        ST<Character, ArrayList<Integer>> firstIndexes = createST(firstColumn);
        int[] next = new int[N];
        for (char c : firstIndexes) {
            ArrayList<Integer> firstCindexes = firstIndexes.get(c);
            ArrayList<Integer> tCindexes = tIndexes.get(c);
            int C = firstCindexes.size();
            for (int i = 0; i < C; i++) next[firstCindexes.get(i)] = tCindexes.get(i);
        }
        return next;
    }

    // create dictionary of indexes
    private static ST<Character, ArrayList<Integer>> createST(char[] a) {
        int N = a.length;
        ST<Character, ArrayList<Integer>> st = new ST<Character, ArrayList<Integer>>();
        for (int i = 0; i < N; i++) {
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            if (st.contains(a[i])) {
                indexes = st.get(a[i]);
            }
            indexes.add(i);
            st.put(a[i], indexes);
        }
        return st;
    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            BurrowsWheeler.transform();
        }
        else {
            BurrowsWheeler.inverseTransform();
        }
    }
}
