/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final RWayTrie<String> words;
    private final ST<Integer, Integer> scores;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException("");
        scores = initScores();
        words = new RWayTrie<String>();
        for (int i = 0; i < dictionary.length; i++) {
            String word = dictionary[i];
            if (word == null) throw new IllegalArgumentException("");
            words.put(word, word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException("");
        int m = board.rows();
        int n = board.cols();
        SET<String> validWords = new SET<String>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boolean[][] marked = new boolean[m][n];
                getValidWordsFromCell(i, j, marked, "", validWords, board);
            }
        }
        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("");
        if (!words.contains(word)) return 0;
        int wordLength = word.length();
        if (word.contains("Qu")) wordLength += 1;
        if (wordLength < 3) return 0;
        if (wordLength >= 8) return 11;
        return scores.get(wordLength);
    }

    // initialize scores symbol table
    private ST<Integer, Integer> initScores() {
        ST<Integer, Integer> gameScores = new ST<Integer, Integer>();
        gameScores.put(3, 1);
        gameScores.put(4, 1);
        gameScores.put(5, 2);
        gameScores.put(6, 3);
        gameScores.put(7, 5);
        return gameScores;
    }

    // generate words for boggle board
    private void getValidWordsFromCell(int i, int j, boolean[][] marked,
                                       String word,
                                       SET<String> validWords, BoggleBoard board) {
        marked[i][j] = true;
        String letter = String.valueOf(board.getLetter(i, j));
        if (letter.equals("Q")) letter = "QU";
        String candidate = word + letter;
        if (!words.containsPrefix(candidate)) return;
        if ((words.contains(candidate)) && (candidate.length() > 2)) validWords.add(candidate);
        int[] neighbors = { -1, 0, 1 };
        for (int neighborI : neighbors) {
            for (int neighborJ : neighbors) {
                if ((neighborI == 0) && (neighborJ == 0)) continue;
                if (i + neighborI < 0) continue;
                if (i + neighborI >= board.rows()) continue;
                if (j + neighborJ < 0) continue;
                if (j + neighborJ >= board.cols()) continue;
                if (!marked[i + neighborI][j + neighborJ]) {
                    getValidWordsFromCell(i + neighborI, j + neighborJ, marked,
                                          candidate,
                                          validWords, board);
                    marked[i + neighborI][j + neighborJ] = false;
                }
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
