package maze;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

public class MazeApp implements Runnable {
    public void run() {
        // Set maze size
        int N = 15;

        // Generate Maze using PrimMST
        EdgeWeightedGraph graph = Maze.createGridGraph(N, N);
        Maze maze = new Maze(graph, N, N);

        // Invoked on the event dispatching thread.
        // Construct and show GUI.
        MazeVisualizer app = new MazeVisualizer(maze);
        app.setSize(900, 600);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MazeApp());
    }
}
