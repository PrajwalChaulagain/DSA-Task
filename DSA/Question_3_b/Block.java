package Question_3_b;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
  This class represents a Tetris-like block that can move, rotate, and track its position on a grid.
  The block is defined by a 2D array representing its shape and a color attribute.
  The algorithm allows:
  Moving the block in the x and y directions.
  Rotating the block 90 degrees clockwise.
  Adjusting the block's position after rotation to maintain its center alignment.
  Retrieving the block's occupied cells for rendering or collision detection.
 */
public class Block {
    private int[][] shape; // 2D array representing the shape of the block
    private Color color; // Color of the block
    private int x; // X-coordinate of the block's top-left corner
    private int y; // Y-coordinate of the block's top-left corner

    /**
      Constructor to initialize the block with a shape and color.
      The block starts at a default position (x = 4, y = 0).
      
      @param shape 2D array defining the block's shape
      @param color Color of the block
     */
    public Block(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
        this.x = 4;  // Default start position in the grid
        this.y = 0;
    }

    /**
      Moves the block by a given offset in the x and y directions.
      
      @param dx Change in x-coordinate
      @param dy Change in y-coordinate
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    /**
      Adjusts the block's x-position to keep it centered after rotation.
      This ensures the block remains in a consistent position relative to its original shape.
      
      @param originalShape The original unrotated shape of the block
     */
    public void centerAdjustment(int[][] originalShape) {
        int xOffset = (originalShape[0].length - shape[0].length) / 2;
        x += xOffset;
    }

    /**
      Rotates the block 90 degrees clockwise.
      The transformation is done by transposing the matrix and reversing each row.
     */
    public void rotate() {
        int[][] rotated = new int[shape[0].length][shape.length];
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                rotated[j][shape.length - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }

    /**
      Returns a list of cells occupied by the block.
      Each cell has an x and y coordinate and the block's color.
      
      @return List of cells occupied by the block
     */
    public List<Cell> getCells() {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    cells.add(new Cell(x + j, y + i, color));
                }
            }
        }
        return cells;
    }

    // Getters and setters for shape, color, and position
    public int[][] getShape() { return shape; }
    public void setShape(int[][] shape) { this.shape = shape; }
    public Color getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
}

/**
  Summary:
  This algorithm effectively handles the movement and rotation of a block in a grid-based game like Tetris.
  - The move() function shifts the block's position.
  - The rotate() function rotates the shape 90 degrees clockwise.
  - The centerAdjustment() function ensures the block remains centered after rotation.
  - The getCells() function retrieves the occupied cells for rendering or collision detection.
 
  The algorithm performs as expected, allowing smooth movement and rotation of blocks in a grid.
  Further improvements could include boundary checks and collision handling to integrate it into a full game.
 */
