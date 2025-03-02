package Question_3_b;
import java.awt.Color;

/**
 * This class represents a single cell in a grid-based game like Tetris.
 * Each cell has x and y coordinates to define its position and a color attribute.
 * 
 * The algorithm enables:
 * - Storing a cell's position and color.
 * - Retrieving and modifying the cell's properties.
 */
public class Cell {
    private int x; // X-coordinate of the cell
    private int y; // Y-coordinate of the cell
    private Color color; // Color of the cell

    /**
     * Constructor to initialize a cell with x, y coordinates, and a color.
     * 
     * @param x The x-coordinate of the cell
     * @param y The y-coordinate of the cell
     * @param color The color of the cell
     */
    public Cell(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /**
     * Retrieves the x-coordinate of the cell.
     * 
     * @return The x-coordinate
     */
    public int getX() { return x; }

    /**
     * Retrieves the y-coordinate of the cell.
     * 
     * @return The y-coordinate
     */
    public int getY() { return y; }

    /**
     * Retrieves the color of the cell.
     * 
     * @return The color of the cell
     */
    public Color getColor() { return color; }

    /**
     * Updates the y-coordinate of the cell.
     * 
     * @param y The new y-coordinate
     */
    public void setY(int y) { this.y = y; }
}

/**
 * Summary:
 * This algorithm defines a simple data structure for representing a cell in a grid.
 * - The constructor initializes the cell's position and color.
 * - Getter methods allow retrieving x, y, and color values.
 * - The setter method allows modifying the y-coordinate.
 *
 * The algorithm functions as expected, providing a foundational building block for more complex grid-based logic.
 * Further improvements could include additional setter methods for x and color, if needed.
 */
