package maze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;

// A Maze is pretty much a minimum spanning tree (MST) of a graph that is structured like a grid.
public class Maze extends PrimMST {
    private int width;
    private int height;
    private List<Edge> mazePaths; // The edges that form our maze

    public Maze(EdgeWeightedGraph G, int width, int height) {
        super(G); // Call parent constructor to build MST
        this.width = width;
        this.height = height;
        this.mazePaths = new ArrayList<>();

        // edges() gives us the MST edges computed from G
        // Convert the MST edges to maze paths
        for (Edge e : edges()) {
            mazePaths.add(e);
        }
    }

    // Create the initial graph with all possible paths
    // Basically, we are taking an edge-weighted graph and shaping it into a
    // connected grid.
    // We assign random values to the weights. This aspect makes our mazes random.
    public static EdgeWeightedGraph createGridGraph(int width, int height) {
        int V = width * height; // Total number of vertices
        EdgeWeightedGraph G = new EdgeWeightedGraph(V);
        Random rand = new Random();

        // Add edges for all possible paths
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int v = gridToVertex(x, y, width);

                // Add horizontal edge (if not at right edge)
                if (x < width - 1) {
                    int u = gridToVertex(x + 1, y, width);
                    Edge e = new Edge(v, u, rand.nextDouble()); // Random weight
                    G.addEdge(e);
                }

                // Add vertical edge (if not at bottom edge)
                if (y < height - 1) {
                    int u = gridToVertex(x, y + 1, width);
                    Edge e = new Edge(v, u, rand.nextDouble()); // Random weight
                    G.addEdge(e);
                }
            }
        }
        return G;
    }

    // Convert grid coordinates to vertex number
    private static int gridToVertex(int x, int y, int width) {
        return y * width + x;
    }

    // Convert vertex number to grid coordinates
    // private Point vertexToGrid(int v) {
    // int x = v % width;
    // int y = v / width;
    // return new Point(x, y);
    // }

    // Check if there's a path between two adjacent cells
    public boolean isPath(Cell a, Cell b) {
        int v1 = gridToVertex(a.getX(), a.getY(), width);
        int v2 = gridToVertex(b.getX(), b.getY(), width);

        for (Edge e : mazePaths) {
            int v = e.either();
            int w = e.other(v);
            if ((v == v1 && w == v2) || (v == v2 && w == v1)) {
                return true;
            }
        }
        return false;
    }

    // Print the maze
    public void printMaze() {
        // Print top border
        for (int x = 0; x < width; x++) {
            System.out.print("+---");
        }
        System.out.println("+");

        // Print maze interior
        for (int y = 0; y < height; y++) {
            // Print cells and vertical walls
            System.out.print("|");
            for (int x = 0; x < width - 1; x++) {
                System.out.print("   ");
                Cell currentCell = new Cell(x, y);
                Cell rightCell = new Cell(x + 1, y);
                System.out.print(isPath(currentCell, rightCell) ? " " : "|");
            }
            System.out.println("   |");

            // Print horizontal walls
            for (int x = 0; x < width; x++) {
                Cell currentCell = new Cell(x, y);
                Cell bottomCell = new Cell(x, y + 1);
                System.out.print(y < height - 1 && isPath(currentCell, bottomCell) ? "+   " : "+---");
            }
            System.out.println("+");
        }
    }

    // Example usage
    public static void main(String[] args) {
        int width = 15;
        int height = 15;

        // Create graph with all possible paths
        EdgeWeightedGraph G = createGridGraph(width, height);

        // Generate maze using Prim's algorithm
        Maze maze = new Maze(G, width, height);

        // Print the maze
        maze.printMaze();

        // TODO: Convert this text version of the maze to a GUI
    }

    public List<Edge> getMazePaths() {
        return mazePaths;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
