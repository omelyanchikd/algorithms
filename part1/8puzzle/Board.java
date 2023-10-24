/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int manhattanDistance = -1;
    private final int size;
    private int iBlank, jBlank;
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Please supply a board!");
        }
        size = tiles.length;
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
                if (board[i][j] == 0) {
                    iBlank = i;
                    jBlank = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append(size + "\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                representation.append(board[i][j]);
                if (j < size - 1) representation.append(" ");
            }
            representation.append("\n");
        }
        return representation.toString();
    }

    // board dimension n
    public int dimension() {
        return size;
    }

    // number of tiles out of place
    public int hamming() {
        int distance = 0;
        for (int i = 0; i < size * size; i++) {
            if (board[i / size][i % size] == 0) continue;
            distance += (board[i / size][i % size] == i + 1) ? 0 : 1;
        }
        return distance;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattanDistance == -1) {
            manhattanDistance = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == 0) continue;
                    manhattanDistance += Math.abs(i - (board[i][j] - 1) / size) + Math.abs(
                            j - (board[i][j] - 1) % size);
                }
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size * size - 1; i++) {
            if (board[i / size][i % size] != i + 1) return false;
        }
        return true;
    }


    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board otherBoard = (Board) y;
        if (otherBoard.dimension() != board.length) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != otherBoard.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> boards = new ArrayList<Board>();
        int[][] neighborCandidates = {
                { iBlank - 1, jBlank }, { iBlank + 1, jBlank }, { iBlank, jBlank - 1 },
                { iBlank, jBlank + 1 }
        };
        for (int[] candidate : neighborCandidates) {
            if (isValid(candidate[0], candidate[1])) {
                int[][] newTiles = new int[size][size];
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        newTiles[i][j] = board[i][j];
                    }
                }
                newTiles[iBlank][jBlank] = board[candidate[0]][candidate[1]];
                newTiles[candidate[0]][candidate[1]] = 0;
                Board newBoard = new Board(newTiles);
                int[] blank = { iBlank, jBlank };
                newBoard.recomputeManhattanDistance(this.manhattan(),
                                                    board[candidate[0]][candidate[1]], candidate,
                                                    blank);
                boards.add(newBoard);
            }
        }
        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int a = 0, b = 1;
        if (board[a / size][a % size] == 0) a = 2;
        if (board[b / size][b % size] == 0) b = 2;
        int[][] newBoard = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        newBoard[a / size][a % size] = board[b / size][b % size];
        newBoard[b / size][b % size] = board[a / size][a % size];
        return new Board(newBoard);
    }

    // check the validity of the coordinates
    private boolean isValid(int i, int j) {
        return (i >= 0) && (i < size) && (j >= 0) && (j < size);
    }

    // recompute manhattan distance
    private void recomputeManhattanDistance(int previousDistance, int value, int[] a, int[] b) {
        if (previousDistance == -1) previousDistance = manhattan();
        int manhattanDistanceA = Math.abs(a[0] - (value - 1) / size) + Math.abs(
                a[1] - (value - 1) % size);
        int manhattanDistanceB = Math.abs(b[0] - (value - 1) / size) + Math.abs(
                b[1] - (value - 1) % size);
        manhattanDistance = previousDistance - manhattanDistanceA + manhattanDistanceB;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println("Hamming: " + board.hamming());
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println(board.isGoal());
        Iterable<Board> neighbors = board.neighbors();
        System.out.println("Neighbors");
        for (Board neighbor : neighbors) {
            System.out.println(neighbor);
        }
    }

}
