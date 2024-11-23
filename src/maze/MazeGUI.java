package Maze;

import edu.princeton.cs.algs4.StdDraw;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class MazeGUI extends JFrame {
    private Maze maze;
    private MazeSolver solver;
    private List<Point> solutionPath;

    public MazeGUI() {
        super("Maze Escape CSIS 2420");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        maze = new Maze(20, 15); // Initialize the maze
        solutionPath = new ArrayList<>();
        solver = new MazeSolver();

        // Control panel (buttons)
        JPanel controlPanel = new JPanel();
        JButton generateButton = new JButton("Generate Maze");
        generateButton.addActionListener(e -> {
            maze.resetMaze(); // Reset and regenerate the maze
            solutionPath.clear(); // Clear the solution path
        });
        controlPanel.add(generateButton);

        JButton solveButton = new JButton("Solve Maze");
        solveButton.addActionListener(e -> {
            solutionPath = solver.solve(maze, "DFS");
            drawSolution(); // Draw the solution using StdDraw
        });
        controlPanel.add(solveButton);

        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to draw the solution path using StdDraw
    private void drawSolution() {
        if (solutionPath != null && !solutionPath.isEmpty()) {
            StdDraw.setPenColor(StdDraw.RED);
            for (Point p : solutionPath) {
                StdDraw.filledSquare(p.y + 0.5, maze.getHeight() - p.x - 0.5, 0.3);
            }
            StdDraw.show();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGUI::new);
    }
}
