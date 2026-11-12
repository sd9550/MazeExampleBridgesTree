package mazeexamplebridgestree;

import java.util.*;
import java.io.*;

/**
 * Maze represents a maze of characters. The goal is to get from the
 * top left corner to the bottom right, following a path of 1's. Arbitrary
 * constants are used to represent locations in the maze that have been TRIED
 * and that are part of the solution PATH.
 *
 * @author Lewis and Chase
 * @version 4.0
 */
public class Maze
{
    private static final int TRIED = 2;
    private static final int PATH = 3;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    private final String FILE_NAME;

    private final int[][] grid;

    /**
     * Constructor for the Maze class. Loads a maze from the given file.  
     * Throws a FileNotFoundException if the given file is not found.
     *
     * @param filename the name of the file to load
     * @throws FileNotFoundException if the given file is not found
     */
    public Maze(String filename) throws FileNotFoundException
    {
        Scanner scan = new Scanner(new File(filename));
        int numberRows = scan.nextInt();
        int numberColumns = scan.nextInt();
        FILE_NAME = filename;
        
        grid = new int[numberRows][numberColumns];
        for (int i = 0; i < numberRows; i++)
            for (int j = 0; j < numberColumns; j++)
                grid[i][j] = scan.nextInt();
    }
        
    /**
     * Marks the specified position in the maze as TRIED
     */
    public void tryPosition(Position position)
    {
        grid[position.getY()][position.getX()] = TRIED;
    }
    
    /**
     * Return the number of rows in this maze
     *
     * @return the number of rows in this maze
     */
    public int getRows()
    {
        return grid.length;
    }
    
    /**
     * Return the number of columns in this maze
     *
     * @return the number of columns in this maze
     */
    public int getColumns()
    {
        return grid[0].length;
    }

    /**
     * Return the name of the file used to create this maze
     *
     * @return the file name string
     */
    public String getFileName() {
        return FILE_NAME;
    }
    
    /**
     * Marks a given position in the maze as part of the PATH
     */
    public void markPath(Position position)
    {

        grid[position.getY()][position.getX()] = PATH;
    }

    /**
     * Determines if a specific location is valid. A valid location
     * is one that is on the grid, is not blocked, and has not been TRIED.
     *
     * @param row the row to be checked
     * @param column the column to be checked
     * @return true if the location is valid    
     */
    public boolean validPosition(Position position)
    {
        boolean result = false;
 
        // check if cell is in the bounds of the matrix 
        if (position.getY() >= 0 && position.getY() < grid.length &&
            position.getX() >= 0 && position.getX() < grid[position.getY()].length)

            //  check if cell is not blocked and not previously tried 
            if (grid[position.getY()][position.getX()] == 1)
                result = true;

        return result;
    }

    /**
     * Returns the maze as a string.
     * 
     * @return a string representation of the maze
     */
    public String toString()
    {
        StringBuilder result = new StringBuilder("\n");

        for (int[] ints : grid) {
            for (int anInt : ints) {
                if (anInt == PATH) {
                    // Marks the path from start to finish with red integers
                    result.append(ANSI_RED).append(anInt).append(ANSI_RESET).append(" ");
                } else {
                    result.append(anInt).append(" ");
                }
            }

            result.append("\n");
        }

        return result.toString();
    }
}
