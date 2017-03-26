/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation
 *  
 *  Author:       xxwywzy
 *  Written:      26/03/2017
 * 
 *  Percolation data tyoe
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
    private int gridSize;
    private boolean[][] grid;
    private WeightedQuickUnionUF quickUnionStructure;
    private WeightedQuickUnionUF quickUnionStructureTop; // prevent from the backwash problem
    private int virtualTopSite;
    private int virtualBottomSite;
    private int openSites;
    
    /**
     * Create n-by-n grid, with all sites blocked
     * 
     * @param n
     *          size of the grid
     */
    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("n must be at least 1");
        }
        gridSize = n;
        grid = new boolean[n][n]; // defult false
        
        quickUnionStructure = new WeightedQuickUnionUF(2 + n * n);
        quickUnionStructureTop = new WeightedQuickUnionUF(1 + n * n);
        
        virtualTopSite = 0;
        virtualBottomSite = n * n + 1;
        
    }
    
    /**
     * Open site (row, col) if it is not open already
     * 
     * @param row
     *          row index
     * 
     * @param col
     *          column index
     */
    public void open(int row, int col) {
        checkInput(row, col);
        if (!isOpen(row, col)) {
            int fieldIndex = xyToID(row, col);
            if (row == 1) {
                quickUnionStructure.union(virtualTopSite, fieldIndex);
                quickUnionStructureTop.union(virtualTopSite, fieldIndex);
            }
            if (row == gridSize) { //do not use else for n equals 1
                quickUnionStructure.union(virtualBottomSite, fieldIndex);
            }
            
            connectIfOpen(fieldIndex, row + 1, col);
            connectIfOpen(fieldIndex, row - 1, col);
            connectIfOpen(fieldIndex, row, col + 1);
            connectIfOpen(fieldIndex, row, col - 1);
            
            grid[row - 1][col - 1] = true;
            openSites++;
        }
    }
    
    /**
     * Is site (row, col) open?
     * 
     * @param row
     *          row index
     * 
     * @param col
     *          column index
     * @return is site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        checkInput(row, col);
        return grid[row - 1][col - 1];
    }
    
    /**
     * Is site (row, col) full?
     * 
     * @param row
     *          row index
     * 
     * @param col
     *          column index
     * @return is site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        checkInput(row, col);
        if (isOpen(row, col)) {
            int fieldIndex = xyToID(row, col);
            return quickUnionStructureTop.connected(virtualTopSite, fieldIndex);
        }
        return false;
    }
    
    /**
     * Number of open sites
     * 
     * @return number of open sites
     */
    public int numberOfOpenSites() {
        return openSites;
    }
    
    /**
     * Does the system percolate?
     * 
     * @return does the system percolate?
     */
    public boolean percolates() {
        return quickUnionStructure.connected(virtualTopSite, virtualBottomSite);
    }
    
    private void connectIfOpen(int fieldIndex, int row, int col) {
        if (!(row < 1 || row > gridSize || col < 1 || col > gridSize)) {
            if (isOpen(row, col)) {
                int neighbourFieldIndex = xyToID(row, col);
                quickUnionStructure.union(neighbourFieldIndex, fieldIndex);
                quickUnionStructureTop.union(neighbourFieldIndex, fieldIndex);
            }
        }
    }
    
    private int xyToID(int row, int col) {
        return (row - 1) * gridSize + col;
    }
    
    private void checkInput(int row, int col) {
        if (row < 1 || row > gridSize || col < 1 || col > gridSize) {
            throw new IndexOutOfBoundsException("row and col index out of bounds");
        }    
    }

    /**
     * Test client (optional)
     */
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        p.open(1, 1);
        StdOut.println(p.percolates()+"");
        StdOut.println(p.numberOfOpenSites()+"");
        
    }
} 