/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private SearchNode previous;

        public SearchNode(Board initial) {
            board = initial;
            moves = 0;
            previous = null;
        }

        public SearchNode(Board newBoard, int previousMoves, SearchNode previousNode) {
            board = newBoard;
            moves = previousMoves;
            previous = previousNode;
        }

        public int compareTo(SearchNode node) {
            return this.moves + board.manhattan() - node.moves - node.board.manhattan();
        }

    }

    private SearchNode finalNode;
    private SearchNode twinFinalNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("You cannot supply an empty board to solve!");
        }
        MinPQ<SearchNode> boardPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinBoardPQ = new MinPQ<SearchNode>();
        boardPQ.insert(new SearchNode(initial));
        twinBoardPQ.insert(new SearchNode(initial.twin()));
        while (true) {
            if (boardPQ.isEmpty()) break;
            SearchNode current = boardPQ.delMin();
            if (current.board.isGoal()) {
                finalNode = current;
                break;
            }
            Iterable<Board> nextBoards = current.board.neighbors();
            for (Board nextBoard : nextBoards) {
                if (current.previous != null) {


                    if (nextBoard.equals(current.previous.board)) continue;
                }
                boardPQ.insert(new SearchNode(nextBoard, current.moves + 1, current));
            }
            if (twinBoardPQ.isEmpty()) break;
            SearchNode currentTwin = twinBoardPQ.delMin();
            if (currentTwin.board.isGoal()) {
                twinFinalNode = currentTwin;
                break;
            }
            Iterable<Board> nextTwinBoards = currentTwin.board.neighbors();
            for (Board nextTwinBoard : nextTwinBoards) {
                if (currentTwin.previous != null) {
                    if (nextTwinBoard.equals(currentTwin.previous.board)) continue;
                }
                twinBoardPQ.insert(
                        new SearchNode(nextTwinBoard, currentTwin.moves + 1, currentTwin));
            }
        }
        if (twinFinalNode != null) {
            finalNode = null;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return (finalNode != null);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return finalNode.moves;
        return -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        LinkedList<Board> boards = new LinkedList<Board>();
        while (finalNode != null) {
            boards.addFirst(finalNode.board);
            finalNode = finalNode.previous;
        }
        return boards;
    }

    // solver step
    private void step(MinPQ<Board> PQ) {

    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
