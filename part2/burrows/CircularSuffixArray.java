/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class CircularSuffixArray {
    private static final int R = 256;
    private final int L;
    private final String input;
    private int[] indexes;
    private int[] counts;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("");
        L = s.length();
        input = s;
        indexes = new int[L];
        for (int i = 0; i < L; i++) indexes[i] = i;
        computeCounts();
        sort();
    }

    // length of s
    public int length() {
        return L;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= L) throw new IllegalArgumentException("");
        return indexes[i];
    }

    // compute counts for characters in the input string
    private void computeCounts() {
        counts = new int[R + 1];
        for (int i = 0; i < L; i++)
            counts[input.charAt(i) + 1]++;
        for (int r = 0; r < R; r++)
            counts[r + 1] += counts[r];
    }

    // sort suffixes
    private void sort() {
        int[] aux = new int[L];
        for (int d = L - 1; d >= 0; d--) {
            int[] characterCounts = new int[R + 1];
            for (int i = 0; i <= R; i++)
                characterCounts[i] = counts[i];
            for (int i = 0; i < L; i++)
                aux[characterCounts[input.charAt((indexes[i] + d) % L)]++] = indexes[i];
            for (int i = 0; i < L; i++)
                indexes[i] = aux[i];
        }
    }

    // unit testing (required)

    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray(args[0]);
        System.out.println(csa.length());
        for (int i = 0; i < args[0].length(); i++) {
            System.out.println(csa.index(i));
        }

    }
}
