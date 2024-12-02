package maze;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;

// A Maze is pretty much a minimum spanning tree (MST) of a graph that is structured like a grid.
public class Maze extends PrimMST {
    private int width;
    private int height;

    // Represents all possible paths that can be taken in the maze
    private List<Edge> mazePaths;

    public Maze(EdgeWeightedGraph G, int width, int height) {
        // Call parent (PrimMST) constructor to build the minimum-spanning tree
        super(G);

        this.width = width;
        this.height = height;
        this.mazePaths = new ArrayList<>();

        // edges() gives us the MST edges (maze paths) computed from G
        for (Edge e : edges()) {
            mazePaths.add(e);
        }
    }

    // Convert grid coordinates to vertex number
    static int gridToVertex(int x, int y, int width) {
        return y * width + x;
    }

    // Check if there's a path (no wall) between two adjacent points in our maze
    public boolean isPath(Point a, Point b) {
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
                Point currentCell = new Point(x, y);
                Point rightCell = new Point(x + 1, y);
                System.out.print(isPath(currentCell, rightCell) ? " " : "|");
            }
            System.out.println("   |");

            // Print horizontal walls
            for (int x = 0; x < width; x++) {
                Point currentCell = new Point(x, y);
                Point bottomCell = new Point(x, y + 1);
                System.out
                        .print(y < height - 1 && isPath(currentCell, bottomCell) ? "+   " : "+---");
            }
            System.out.println("+");
        }
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
