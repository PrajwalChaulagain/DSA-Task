package Question_3_b;
import javax.swing.*;
import java.awt.*;

/**
 * The GameBoard class represents the visual rendering of the Tetris game.
 * It extends JPanel and is responsible for drawing the grid, placed blocks,
 * the currently falling block, and the next preview block.
 * 
 * The algorithm achieves:
 * - Rendering the game grid.
 * - Displaying the blocks that have already been placed.
 * - Drawing the currently active block in motion.
 * - Showing a preview of the next block.
 */
public class GameBoard extends JPanel {
    private final TetrisGame game; // Reference to the game instance

    /**
     * Constructor to initialize the game board panel with the given game instance.
     * 
     * @param game The Tetris game instance
     */
    public GameBoard(TetrisGame game) {
        this.game = game;
        setPreferredSize(new Dimension(450, 600));
    }

    /**
     * Overrides the paintComponent method to render the game board.
     * Calls helper methods to draw the grid, blocks, current block, and preview block.
     * 
     * @param g The Graphics object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawBlocks(g);
        drawCurrentBlock(g);
        drawPreview(g);
    }

    /**
     * Draws the grid lines on the game board to create a structured play area.
     * 
     * @param g The Graphics object used for drawing
     */
    private void drawGrid(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x < TetrisGame.WIDTH; x++) {
            for (int y = 0; y < TetrisGame.HEIGHT; y++) {
                g.drawRect(x * 30, y * 30, 30, 30);
            }
        }
    }

    /**
     * Draws the blocks that have already been placed on the game grid.
     * 
     * @param g The Graphics object used for drawing
     */
    private void drawBlocks(Graphics g) {
        boolean[][] grid = game.getGrid();
        Color[][] colors = game.getColors();
        for (int x = 0; x < TetrisGame.WIDTH; x++) {
            for (int y = 0; y < TetrisGame.HEIGHT; y++) {
                if (grid[x][y]) {
                    g.setColor(colors[x][y]);
                    g.fillRect(x * 30 + 1, y * 30 + 1, 28, 28);
                }
            }
        }
    }

    /**
     * Draws the currently falling block on the game board.
     * 
     * @param g The Graphics object used for drawing
     */
    private void drawCurrentBlock(Graphics g) {
        Block current = game.getCurrentBlock();
        if (current != null) {
            g.setColor(current.getColor());
            for (Cell cell : current.getCells()) {
                int x = cell.getX();
                int y = cell.getY();
                if (y >= 0) {
                    g.fillRect(x * 30 + 1, y * 30 + 1, 28, 28);
                }
            }
        }
    }

    /**
     * Draws the preview of the next block in a designated area of the UI.
     * 
     * @param g The Graphics object used for drawing
     */
    private void drawPreview(Graphics g) {
        int previewX = 330;
        int previewY = 50;
        int previewSize = 100;

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(previewX, previewY, previewSize, previewSize);
        g.setColor(Color.BLACK);
        g.drawString("Next Block:", previewX + 10, previewY - 10);

        Block next = game.getNextBlock();
        if (next != null) {
            int[][] shape = next.getShape();
            int startX = previewX + (previewSize - shape[0].length * 20) / 2;
            int startY = previewY + (previewSize - shape.length * 20) / 2;

            g.setColor(next.getColor());
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        g.fillRect(startX + j * 20, startY + i * 20, 19, 19);
                    }
                }
            }
        }
    }
}

/**
 * Summary:
 * This algorithm successfully renders the game board by drawing:
 * - The game grid using drawGrid().
 * - The blocks that have been placed using drawBlocks().
 * - The current falling block using drawCurrentBlock().
 * - The preview of the next block using drawPreview().
 *
 * The algorithm works as expected, ensuring proper rendering of game elements.
 * Future improvements could include adding animations and improving color contrast for better visibility.
 */
