package maze;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;

public class MazeApp implements Runnable {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MazeApp());
    }

    public void run() {
        // Set maze size
        int N = 15;

        EdgeWeightedGraph graph = createGridGraph(N, N);

        // Generate Maze using PrimMST
        Maze maze = new Maze(graph, N, N);

        // Invoked on the event dispatching thread.
        // Construct and show GUI.
        MazeVisualizer app = new MazeVisualizer(maze);
        app.setSize(900, 600);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Create the initial graph with no edges (this means that there are all walls and no paths
    // initially)
    // We either add a vertical edge or horizontal edge (no diag edges, because we are creating a
    // maze)
    // We assign random values to the weights. This aspect makes our maze generation random.
    // The result of this method gives us a graph that looks like a grid. Each edge in the
    // graph/grid has a random weight assigned to it.
    public static EdgeWeightedGraph createGridGraph(int width, int height) {
        int V = width * height; // Total number of vertices
        EdgeWeightedGraph G = new EdgeWeightedGraph(V);
        Random rand = new Random();

        // Add edges for all possible paths
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = Maze.gridToVertex(x, y, width);

                // Add horizontal edge (if not at right edge)
                // If our x-coordinate is within bounds
                if (x < width - 1) {
                    // Convert (x,y) to a vertex number
                    int u = Maze.gridToVertex(x + 1, y, width);

                    Edge e = new Edge(v, u, rand.nextDouble()); // Random weight
                    G.addEdge(e);
                }

                // Add vertical edge (if not at bottom edge)
                // If our y-coordinate is within bounds
                if (y < height - 1) {
                    int u = Maze.gridToVertex(x, y + 1, width);
                    Edge e = new Edge(v, u, rand.nextDouble()); // Random weight
                    G.addEdge(e);
                }
            }
        }
        return G;
    }
}
