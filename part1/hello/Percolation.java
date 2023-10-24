/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class Percolation {
    private int N;
    private boolean[] cells;
    private int[] trees;
    private int[] treeSizes;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Provide n should be greater than 0!");
        }
        N = n;
        int gridSize = N * N + 1;
        cells = new boolean[gridSize];
        trees = new int[gridSize];
        treeSizes = new int[gridSize];
        for (int i = 0; i < gridSize; i++) {
            cells[i] = false;
            trees[i] = i;
            treeSizes[i] = 1;
        }
        cells[0] = true;
        treeSizes[0] = N + 1;
        for (int i = 0; i < N; i++) {
            trees[i + 1] = 0;
        }
    }

    // find root
    private int root(int p) {
        while (p != trees[p]) {
            trees[p] = trees[trees[p]];
            p = trees[p];
        }
        return p;
    }

    // connect open cell by index
    private void connect(int indexA, int indexB) {
        int rootA = root(indexA);
        int rootB = root(indexB);
        if (rootA == rootB) {
            return;
        }
        if (treeSizes[rootA] < treeSizes[rootB]) {
            trees[rootA] = rootB;
            treeSizes[rootB] += treeSizes[rootA];
        }
        else {
            trees[rootB] = rootA;
            treeSizes[rootA] += treeSizes[rootB];
        }
    }

    // connect open cells
    private void connect(int[] a, int[] b) {
        int indexA = (a[0] - 1) * N + a[1];
        int indexB = (b[0] - 1) * N + b[1];
        connect(indexA, indexB);
    }

    // is valid cell?
    private boolean isValid(int[] ar) {
        int row = ar[0];
        int col = ar[1];
        return (row <= N) && (row >= 1) && (col <= N) && (col >= 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > N) || (col > N)) {
            throw new IllegalArgumentException("Row and column indices should be between 1 and n");
        }
        if (!(isOpen(row, col))) {
            cells[(row - 1) * N + col] = true;
            int[] cell = { row, col };
            int[][] neighbors = {
                    { row - 1, col }, { row + 1, col }, { row, col - 1 }, { row, col + 1 }
            };
            for (int[] neighbor : neighbors) {
                if ((isValid(neighbor)) && (isOpen(neighbor[0], neighbor[1]))) {
                    connect(cell, neighbor);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > N) || (col > N)) {
            throw new IllegalArgumentException("Row and column indices should be between 1 and n");
        }
        return (cells[(row - 1) * N + col]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if ((row <= 0) || (col <= 0) || (row > N) || (col > N)) {
            throw new IllegalArgumentException("Row and column indices should be between 1 and n");
        }
        if (!isOpen(row, col)) {
            return false;
        }
        return (root(0) == root((row - 1) * N + col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i = 1; i < N * N + 1; i++) {
            if (cells[i]) {
                openSites += 1;
            }
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        int rootTop = root(0);
        for (int col = 0; col < N; col++) {
            if (root((N - 1) * N + col) == rootTop) {
                return true;
            }
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 3;
        Percolation percolation = new Percolation(n);
        System.out.println("Point (1, 1) is open? " + percolation.isOpen(1, 1));
        System.out.println("Point (1, 1) is full? " + percolation.isFull(1, 1));
        percolation.open(1, 1);
        System.out.println("Point (1, 1) is open? " + percolation.isOpen(1, 1));
        System.out.println("Point (1, 1) is full? " + percolation.isFull(1, 1));
        System.out.println("Grid percolates? " + percolation.percolates());
        percolation.open(3, 1);
        System.out.println("Point (3, 1) is open? " + percolation.isOpen(3, 1));
        System.out.println("Point (3, 1) is full? " + percolation.isFull(3, 1));
        percolation.open(2, 1);
        System.out.println("Point (2, 1) is open? " + percolation.isOpen(2, 1));
        System.out.println("Point (2, 1) is full? " + percolation.isFull(2, 1));
        System.out.println("Point (3, 1) is full? " + percolation.isFull(3, 1));
        System.out.println("Grid percolates? " + percolation.percolates());
        percolation.open(3, 3);
        System.out.println("Point (3, 3) is open? " + percolation.isOpen(3, 3));
        System.out.println("Point (3, 3) is full? " + percolation.isFull(3, 3));
        System.out.println("Grid percolates? " + percolation.percolates());

    }
}
