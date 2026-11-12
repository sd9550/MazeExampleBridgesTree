package mazeexamplebridgestree;
/**
 * Represents a single position in a maze of characters.
 *
 * @author Lewis and Chase
 * @version 4.0
 */
public class Position
{
    private int x; 
    private int y;

    /**
     * Constructs a position and sets the x & y coordinates to 0,0.
     */
    Position ()
    {
        x = 0;
        y = 0;
    }
    
    /**
     * Constructs a position and sets the x & y coordinates to 0,0.
     */
    Position (int inX, int inY)
    {
        x = inX;
        y = inY;
    }

    /**
     * Returns the x-coordinate value of this position.
     * @return the x-coordinate of this position
     */
    public int getX()
    {
        return x;
    }

    /**
     * Returns the y-coordinate value of this position.
     * @return the y-coordinate of this position
     */
    public int getY()
    {
        return y;
    }

    /**
     * Sets the value of the current position's x-coordinate.
     * @param a value of x-coordinate
     */
    public void setX(int a)
    {
        x = a;
    }

    /**
     * Sets the value of the current position's x-coordinate.
     * @param a value of y-coordinate
     */ 
    public void setY(int a)
    {
        y = a;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
