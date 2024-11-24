package maze;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import edu.princeton.cs.algs4.EdgeWeightedGraph;

public class MazeVisualizer extends JFrame {
    private JPanel mainPanel;
    private Maze maze;

    public MazeVisualizer(Maze maze) {
        this.maze = maze;

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel with padding
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setLayout(new BorderLayout());

        // Add the panel to the frame
        setContentPane(mainPanel);

        // Set window title
        setTitle(maze.getHeight() + "x" + maze.getWidth() + " maze");

        // Size and center the window
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // Make sure heavyweight components are painted properly
        setBackground(new Color(240, 240, 240));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void drawMaze(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = maze.getWidth();
        int height = maze.getHeight();

        int cellWidth = mainPanel.getWidth() / width;
        int cellHeight = mainPanel.getHeight() / height;

        // Set line properties
        g2d.setStroke(new BasicStroke(8));
        g2d.setColor(Color.BLACK);

        // Draw all walls first
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = new Cell(x, y);
                int cellX = cell.getX() * cellWidth;
                int cellY = cell.getY() * cellHeight;

                // Draw right wall if needed
                if (cell.getX() < width - 1) {
                    Cell rightCell = new Cell(cell.getX() + 1, cell.getY());
                    if (!maze.isPath(cell, rightCell)) {
                        g2d.drawLine(cellX + cellWidth, cellY,
                                cellX + cellWidth, cellY + cellHeight);
                    }
                } else {
                    // Always draw right border wall
                    g2d.drawLine(cellX + cellWidth, cellY,
                            cellX + cellWidth, cellY + cellHeight);
                }

                // Draw bottom wall if needed
                if (cell.getY() < height - 1) {
                    Cell bottomCell = new Cell(cell.getX(), cell.getY() + 1);
                    if (!maze.isPath(cell, bottomCell)) {
                        g2d.drawLine(cellX, cellY + cellHeight,
                                cellX + cellWidth, cellY + cellHeight);
                    }
                } else {
                    // Always draw bottom border wall
                    g2d.drawLine(cellX, cellY + cellHeight,
                            cellX + cellWidth, cellY + cellHeight);
                }

                // Always draw left border wall for first column
                if (cell.getX() == 0) {
                    g2d.drawLine(cellX, cellY, cellX, cellY + cellHeight);
                }

                // Always draw top border wall for first row
                if (cell.getY() == 0) {
                    g2d.drawLine(cellX, cellY, cellX + cellWidth, cellY);
                }
            }
        }

        // Draw start and end points
        int markerSize = Math.min(cellWidth, cellHeight) / 3;

        // Start point (top-left)
        g2d.setColor(Color.GREEN);
        g2d.fillOval(cellWidth / 4, cellHeight / 4, markerSize, markerSize);

        // End point (bottom-right)
        g2d.setColor(Color.RED);
        g2d.fillOval((width - 1) * cellWidth + cellWidth / 4,
                (height - 1) * cellHeight + cellHeight / 4,
                markerSize, markerSize);
    }

    public static void main(String[] args) {
        // Set maze size
        int N = 30;

        // Generate Maze using PrimMST
        EdgeWeightedGraph graph = Maze.createGridGraph(N, N);
        Maze maze = new Maze(graph, N, N);

        // Draw the Maze on the screen
        MazeVisualizer visualizer = new MazeVisualizer(maze);
        visualizer.setVisible(true);
        visualizer.mainPanel.repaint(); // Trigger the drawing
    }
}
