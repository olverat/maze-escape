package maze;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

/**
 * This class is a GUI Swing application that visualizes the solving of a maze using different
 * solving techniques (algorithms).
 * 
 * Note: The main JFrame for this application uses a BorderLayout (see
 * https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html), with JPanels nested inside,
 * each with their own individual layouts.
 */
public class MazeVisualizer extends JFrame {
    private JPanel mazePanel; // Main maze visualization panel - gets all extra space
    private JPanel controlPanel; // The sidebar panel for controlling maze creation and animations.
                                 // This will be a JPanel that holds other JPanels
    private JPanel mazeControls; // Sub-panel to configure maze creation
    private JPanel algorithmControls; // Sub-panel to select the maze solving method
    private JPanel animationControls; // Sub-panel to playback the animation in real-time
    private JPanel eastRegion; // Dummy panel for right margin
    private JPanel statusBar; // Optional: Status bar at bottom
    private Maze maze; // The maze object to be drawn to the screen

    public MazeVisualizer(Maze maze) {
        this.maze = maze;

        // Set the layout for this JFrame
        setLayout(new BorderLayout(10, 10)); // 10px gaps between regions

        // Create all necessary JPanels
        initializePanels();

        // Setup layout code for all panels
        layoutPanels();

        // Add components to the panels
        createMazeControls();
        createAlgorithmControls();
        createAnimationControls();

        // After components are initialized and created, add them to the main JFrame
        // Maze visualizer
        add(mazePanel, BorderLayout.CENTER);

        // Control panel for configuring the maze and solving
        // This code adds spacing between panels
        controlPanel.add(Box.createVerticalStrut(10));
        controlPanel.add(mazeControls);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(algorithmControls);
        controlPanel.add(Box.createVerticalStrut(20));
        controlPanel.add(animationControls);
        controlPanel.add(Box.createVerticalGlue()); // Push everything up

        // Add it to the main JFrame
        add(controlPanel, BorderLayout.WEST);

        // Status bar at the bottom to describe what our program is currently doing
        statusBar.add(new JLabel("Status: Ready"));
        add(statusBar, BorderLayout.SOUTH);

        // Dummy panel for right margin
        add(eastRegion, BorderLayout.EAST);
    }

    private void initializePanels() {
        // Our mazePanel will be a custom JPanel with its paintComponent method overridden for
        // drawing.
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Your maze rendering code here
                drawMaze(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 600); // Default size for maze
            }
        };

        controlPanel = new JPanel();
        animationControls = new JPanel(new FlowLayout(FlowLayout.CENTER));
        algorithmControls = new JPanel(new GridLayout(0, 1, 0, 5)); // vertical grid
        mazeControls = new JPanel(new GridBagLayout()); // For label alignment
        // mazeControls = new DebugPanel(new GridBagLayout());
        eastRegion = new JPanel();
        statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    }

    /**
     * Sets layout related code for all involved JPanels
     */
    private void layoutPanels() {
        // Use BoxLayout for vertical stacking of control elements
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        // Fixed width for control panel, full height
        // Make sure the panel takes full height of its container
        controlPanel.setPreferredSize(new Dimension(300, 0));
        controlPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
        controlPanel.setMinimumSize(new Dimension(300, 0));

        // For each component group, set its max size (fixed width, preferred height)
        mazeControls.setPreferredSize(new Dimension(300, 200));
        mazeControls.setMaximumSize(new Dimension(300, mazeControls.getPreferredSize().height));

        // algorithmControls.setPreferredSize(new Dimension(300, 100));
        algorithmControls
                .setMaximumSize(new Dimension(300, algorithmControls.getPreferredSize().height));

        // animationControls.setPreferredSize(new Dimension(300, 100));
        animationControls
                .setMaximumSize(new Dimension(300, animationControls.getPreferredSize().height));
    }

    /**
     * Adds the necessary components to the maze controls JPanel
     */
    private void createMazeControls() {
        mazeControls.setBorder(BorderFactory.createTitledBorder("Generate Maze"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Left-align components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow components to stretch
        gbc.insets = new Insets(2, 5, 2, 5); // Add some padding
        gbc.weighty = 0;

        // Width label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        JLabel widthLabel = new JLabel("Width:");
        mazeControls.add(widthLabel, gbc);

        // Width field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField widthField = new JTextField("20", 5);
        widthField.setPreferredSize(new Dimension(20, 20)); // Set preferred size
        mazeControls.add(widthField, gbc);

        // Height label
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        JLabel heightLabel = new JLabel("Height:");
        mazeControls.add(heightLabel, gbc);

        // Height field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField heightField = new JTextField("20", 5);
        heightField.setPreferredSize(new Dimension(20, 20)); // Set preferred size
        mazeControls.add(heightField, gbc);

        // Generate button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Still span both columns
        gbc.weightx = 0.0; // Don't take extra space
        gbc.fill = GridBagConstraints.NONE; // Don't stretch
        gbc.anchor = GridBagConstraints.CENTER; // Center in the spanned cells
        JButton generateMazeBtn = new JButton("Generate Maze!");
        mazeControls.add(generateMazeBtn, gbc);
    }

    /**
     * Adds the necessary components to the algorithm controls JPanel
     */
    private void createAlgorithmControls() {
        algorithmControls.setBorder(BorderFactory.createTitledBorder("Maze Solver Technique"));
        ButtonGroup algorithmGroup = new ButtonGroup();
        JRadioButton dfsButton = new JRadioButton("DFS");
        JRadioButton bfsButton = new JRadioButton("BFS");
        JButton solveMazeBtn = new JButton("Solve Maze!");
        algorithmGroup.add(dfsButton);
        algorithmGroup.add(bfsButton);
        algorithmControls.add(dfsButton);
        algorithmControls.add(bfsButton);
        algorithmControls.add(solveMazeBtn);
    }

    /**
     * Adds the necessary components to the animation controls JPanel
     */
    private void createAnimationControls() {
        // Animation controls group
        animationControls.setBorder(BorderFactory.createTitledBorder("Animation Controls"));
        animationControls.add(new JButton("⏮"));
        animationControls.add(new JButton("⏯"));
        animationControls.add(new JButton("⏭"));
    }

    /**
     * Draws a Maze to the screen.
     * 
     * @param g - The Graphics object for this JPanel used for drawing to the screen.
     */
    private void drawMaze(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int width = maze.getWidth();
        int height = maze.getHeight();

        int cellWidth = mazePanel.getWidth() / width;
        int cellHeight = mazePanel.getHeight() / height;

        // Set line properties
        g2d.setStroke(new BasicStroke(8));

        // Draw all walls first
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Point currentCell = new Point(x, y);
                int cellX = currentCell.getX() * cellWidth;
                int cellY = currentCell.getY() * cellHeight;

                // Draw right wall if needed
                if (currentCell.getX() < width - 1) {
                    Point rightCell = new Point(currentCell.getX() + 1, currentCell.getY());
                    if (!maze.isPath(currentCell, rightCell)) {
                        g2d.drawLine(cellX + cellWidth, cellY, cellX + cellWidth,
                                cellY + cellHeight);
                    }
                } else {
                    // Always draw right border wall
                    g2d.drawLine(cellX + cellWidth, cellY, cellX + cellWidth, cellY + cellHeight);
                }

                // Draw bottom wall if needed
                if (currentCell.getY() < height - 1) {
                    Point bottomCell = new Point(currentCell.getX(), currentCell.getY() + 1);
                    if (!maze.isPath(currentCell, bottomCell)) {
                        g2d.drawLine(cellX, cellY + cellHeight, cellX + cellWidth,
                                cellY + cellHeight);
                    }
                } else {
                    // Always draw bottom border wall
                    g2d.drawLine(cellX, cellY + cellHeight, cellX + cellWidth, cellY + cellHeight);
                }

                // Always draw left border wall for first column
                if (currentCell.getX() == 0) {
                    g2d.drawLine(cellX, cellY, cellX, cellY + cellHeight);
                }

                // Always draw top border wall for first row
                if (currentCell.getY() == 0) {
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
                (height - 1) * cellHeight + cellHeight / 4, markerSize, markerSize);
    }

    // For debugging (can ignore): shows outlines on JPanels
    private void toggleOutlines() {
        mazePanel.setBorder(new LineBorder(Color.GREEN, 2));
        controlPanel.setBorder(new LineBorder(Color.BLUE, 2)); // Blue WEST outline
        eastRegion.setBorder(new LineBorder(Color.MAGENTA, 2)); // Magenta EAST outline
    }
}


// Custom panel that paints grid lines for debugging purposes (can ignore)
class DebugPanel extends JPanel {
    public DebugPanel(GridBagLayout l) {
        super(l);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        showDebugGridLines(g);
    }

    private void showDebugGridLines(Graphics g) {
        // Cast to Graphics2D for better rendering
        Graphics2D g2d = (Graphics2D) g.create();

        // Set up the stroke for grid lines
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f,
                new float[] {5.0f}, 0.0f));

        // Draw vertical lines
        int totalWidth = getWidth();
        int cellWidth = totalWidth / 3; // We have 3 columns
        for (int x = 0; x <= totalWidth; x += cellWidth) {
            g2d.drawLine(x, 0, x, getHeight());
        }

        // Draw horizontal lines
        int totalHeight = getHeight();
        int cellHeight = totalHeight / 3; // We have 3 rows
        for (int y = 0; y <= totalHeight; y += cellHeight) {
            g2d.drawLine(0, y, totalWidth, y);
        }

        g2d.dispose();
    }
}
