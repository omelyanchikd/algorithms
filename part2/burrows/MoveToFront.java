/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static final int L = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        int[] alphabet = new int[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char input = BinaryStdIn.readChar(L);
            int position = alphabet[input];
            for (int i = 0; i < R; i++) {
                if (alphabet[i] < position) {
                    alphabet[i] += 1;
                }
            }
            alphabet[input] = 0;
            BinaryStdOut.write(position, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alphabet = new char[R];
        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int input = BinaryStdIn.readChar(L);
            char c = alphabet[input];
            System.arraycopy(alphabet, 0, alphabet, 1, input);
            alphabet[0] = c;
            BinaryStdOut.write(c, 8);
        }
        BinaryStdOut.close();
    }

    // get elements from

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("+")) {
            MoveToFront.decode();
        }
        else {
            MoveToFront.encode();
        }
    }
}
