/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats
 *  
 *  Author:       xxwywzy
 *  Written:      26/03/2017
 * 
 *  Percolation statistics data tyoe
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double coLo;
    private double coHi;
    private double[] thresholds;
    private int times;
    
    /**
     * Perform trials independent experiments on an n-by-n grid
     * 
     * @param n
     *          size of the grid
     * @param trials
     *          number of trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The grid size and trials must be bigger than zero");
        }
        
        times = trials;
        thresholds = new double[trials];
        
        for (int i = 0; i < trials; i++) {
            int counter = 0;
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int x = StdRandom.uniform(n) + 1;
                int y = StdRandom.uniform(n) + 1;
                if (!p.isOpen(x, y)) {
                    p.open(x, y);
                    counter++;
                }
            }
            thresholds[i] = counter / (double) (n * n);           
        }    
    }
    
    /**
     * Sample mean of percolation threshold
     * 
     * @return sample mean of percolation threshold
     */
    public double mean() {
        if (mean == 0)
            mean = StdStats.mean(thresholds);
        return mean;
    }
    
    /**
     * Sample standard deviation of percolation threshold
     * 
     * @return sample standard deviation of percolation threshold
     */
    public double stddev() {
        if (stddev == 0)
            stddev = StdStats.stddev(thresholds);
        return stddev;
    }
    
    /**
     * Low endpoint of 95% confidence interval
     * 
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        if (coLo == 0) { 
            coLo = mean() - (1.96 * stddev())/(Math.sqrt(times));
        }
        return coLo;
    }
    
    /**
     * High endpoint of 95% confidence interval
     * 
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        if (coHi == 0) { 
            coHi = mean() + (1.96 * stddev())/(Math.sqrt(times));
        }
        return coHi;
    }

    /**
     * Test client (described below)
     */
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        
        PercolationStats ps = new PercolationStats(n, t);
        double mean = ps.mean();
        double stddev = ps.stddev();
        double cLo = ps.confidenceLo();
        double cHi = ps.confidenceHi();
        StdOut.println("mean                    = " + mean);
        StdOut.println("stddev                  = " + stddev);
        StdOut.println("95% confidence interval = [" + cLo + ", " + cHi + "]");
    }
}