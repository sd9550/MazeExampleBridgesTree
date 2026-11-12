package mazeexamplebridgestree;
import bridges.base.TreeElement;
import bridges.connect.Bridges;
import bridges.validation.RateLimitException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * MazeSolver attempts to recursively traverse a Maze. The goal is to get from the
 * given starting position to the bottom right, following a path of 1's. Arbitrary
 * constants are used to represent locations in the maze that have been TRIED
 * and that are part of the solution PATH.
 *
 * @author Lewis and Chase
 * @version 4.0
 */
public class MazeSolver
{
    // I had free time, so I added more functionality to the program
    private static final Map<Integer, String> mazeMap = new HashMap<>();
    private static final String TEST_FILE_1 = "testfile.txt";
    private static final String TEST_FILE_2 = "testfile2.txt";
    private static final String TEST_FILE_3 = "testfile3.txt";
    private static final String TEST_FILE_4 = "testfile4.txt";

    /**
     * Creates a new maze, prints its original form, attempts to
     * solve it, and prints out its final form.
     */
    public static void main(String[] args) throws IOException, RateLimitException
    {
        // Get the file the user wants to use.  Options are:
        // * testfile.txt  - small solvable maze.
        // * testfile2.txt - large unsolvable maze.
        // * testfile3.txt - large solvable maze.
        // * testfile4.txt - medium solvable maze.

        mazeMap.put(1, TEST_FILE_1);
        mazeMap.put(2, TEST_FILE_2);
        mazeMap.put(3, TEST_FILE_3);
        mazeMap.put(4, TEST_FILE_4);
        
        Scanner scan = new Scanner(System.in);
        boolean validEntry = false;
        Maze maze = null;

        // Ask the user to select a file to create the Maze; if invalid keep trying
        do {
            // Hard-coding the Maze #4 for this project.
            // Maze #1 is trivial - there is only one path, no branches.
            // Mazes #2-3 are too big for BRIDGES (tree cannot have more than 180 levels).
            try {
                mazeMap.forEach((key, value) -> System.out.println(key + " -> " + value));
                System.out.print("Enter a number to load the corresponding maze file: ");

                String userEntry = scan.nextLine();
                int userSelection = Integer.parseInt(userEntry);
                String filename = mazeMap.get(userSelection);

                // Load the maze and display it to the user.
                maze = new Maze(filename);
                System.out.println(maze);
                validEntry = true;

            } catch (NumberFormatException numberFormatException) {
                System.out.println("\nNumber format exception occurred: " + numberFormatException.getLocalizedMessage());
                System.out.println("Please try again\n");
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("\nFile not found: " + fileNotFoundException.getLocalizedMessage());
                System.out.println("Please try again\n");
            } catch (Exception e) {
                System.out.println("\nAn exception occurred: " + e.getLocalizedMessage());
                System.out.println("Please try again\n");
            }

        } while (!validEntry);
        
        // Create starting point (default is upper-left corner) and push it onto the stack.
        Position position = new Position();

        // Create the tree's root note.
        TreeElement<Position> root = new TreeElement<>(position.toString(), position);
        root.setColor("red");
        
        // Traverse the maze, build the tree, and print out the result.
        if (traverse(maze, position, root))
        {
            System.out.println("The maze was successfully traversed!");
        }
        else
        {
            System.out.println("There is no possible path.");
        }

        if (maze.getFileName().equals(TEST_FILE_1) || maze.getFileName().equals(TEST_FILE_4)) {
            // Create the Bridges project.
            Bridges bridges = new Bridges(
                    0,
                    System.getenv("BRIDGES_USER_ID"),
                    System.getenv("BRIDGES_API_KEY")
            );

            // Set the project's title and description.
            bridges.setTitle("Maze Paths");
            String description = "A tree showing possible paths through a maze.";
            bridges.setDescription(description);

            // Visualize the tree

            bridges.setDataStructure(root);
            bridges.visualize();
            System.out.println(maze);
        } else {
            // Just show the finished maze otherwise
            System.out.println(maze);
        }
    }
    
    /**
     * Attempts to recursively traverse the maze. Inserts special
     * characters indicating locations that have been TRIED and that
     * eventually become part of the solution PATH.
     *
     * @param maze maze we are trying to solve
     * @param position where we are in the maze - the position we are examining.
     * @param treeElement TreeElement representing this position in the maze.
     * @return true if the maze has been solved
     */
    public static boolean traverse(Maze maze, Position position, TreeElement<Position> treeElement)
    {
        // Mark this position as tried.
        maze.tryPosition(position);
        boolean done = false;
            
        //Uncomment to see the progress of the loop.
        //System.out.println(maze);
        
        if (position.getY() == maze.getRows()-1 && position.getX() == maze.getColumns()-1)
        {
            // We have successfully found the exit.
            done = true;
        }
        else
        {
            // Have not found the exit yet.
            // Get a list of directions to try (places we haven't been yet, if any).
            ArrayList<Position> directionsToTry = getDirectionsToTry(position, maze);            

            // For each direction we haven't been yet,
            // Create a child TreeElement and add it to the parent.
            // Go down this path by recursively calling traverse.
            // Stop if we find the exit or there are no more directions to try.
            Position direction;
            for (int index = 0; index < directionsToTry.size() && !done; index++)
            {
                direction = directionsToTry.get(index);

                TreeElement<Position> childElement = new TreeElement<>(direction.toString(), direction);
                treeElement.addChild(childElement);

                done = traverse(maze, direction, childElement);
            }
        }
        
        // If we found the exit, mark this position as being in the solution path.
        if (done)
        {
            // Mark the path from start to end green, otherwise yellow
            treeElement.setColor("green");
            maze.markPath(position);
        } else {
            treeElement.setColor("yellow");
        }
        
        return done;
    }
    
    private static ArrayList<Position> getDirectionsToTry(Position position, Maze maze)
    {
        // Create list of possible directions to try.
        ArrayList<Position> possibleDirectionsToTry = new ArrayList<>();
        possibleDirectionsToTry.add(new Position(position.getX(), position.getY() + 1)); // Down
        possibleDirectionsToTry.add(new Position(position.getX(), position.getY() - 1)); // Up
        possibleDirectionsToTry.add(new Position(position.getX() + 1, position.getY())); // Right
        possibleDirectionsToTry.add(new Position(position.getX() - 1, position.getY())); // Left
        
        // Queue to store the directions we can try from this position.
        ArrayList<Position> directionsToTry = new ArrayList<>();
        
        // Add Positions to the queue only after verifying they are valid.
        for(Position direction: possibleDirectionsToTry)
        {
            if (maze.validPosition(direction))
            {
                directionsToTry.add(direction);
            }
        }
    
        return directionsToTry;
    }

}