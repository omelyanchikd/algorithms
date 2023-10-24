/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private int t;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        results = new double[trials];
        if ((n <= 0) || (trials <= 0)) {
            throw new IllegalArgumentException(
                    "Number of trials and size of grid should be positive!");
        }
        t = trials;
        for (int i = 0; i < trials; i++) {
            results[i] = performTrial(n);
        }
    }

    // perform trial
    private double performTrial(int n) {
        Percolation percolation = new Percolation(n);
        int[] blockedSites = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            blockedSites[i] = 1;
        }
        while (!percolation.percolates()) {
            int siteToOpen = StdRandom.discrete(blockedSites);
            int col = siteToOpen % n + 1;
            int row = siteToOpen / n + 1;
            blockedSites[siteToOpen] = 0;
            percolation.open(row, col);
        }
        return (double) percolation.numberOfOpenSites() / (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceHi() + ", "
                                   + percolationStats.confidenceLo() + "]");

    }

}
