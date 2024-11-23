package Maze;

import edu.princeton.cs.algs4.StdDraw;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import edu.princeton.cs.algs4.Stack;

public class Maze {
    private int width;
    private int height;
    private boolean[][] mazeGrid;
    private Point startPoint;
    private Point endPoint;

    // Constructor
    public Maze(int width, int height) {
        this.width = width;
        this.height = height;
        this.mazeGrid = new boolean[height][width]; 
        this.startPoint = new Point(0, 0);          
        this.endPoint = new Point(height - 1, width - 1); 
        initStdDraw(); // Initialize StdDraw
        generateMaze("DFS");  
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    // Checks if a point in the maze is a wall
    public boolean isWall(Point p) {
        if (p.x < 0 || p.x >= height || p.y < 0 || p.y >= width) {
            return true; 
        }
        return mazeGrid[p.x][p.y];
    }

    // Method to generate the maze (randomized DFS)
    public void generateMaze(String algorithm) {
        if (algorithm.equals("DFS")) {
            generateMazeWithDFS();
        }
        // Additional algorithms can be added later
    }

    // Recursive Backtracker Algorithm for maze generation
    private void generateMazeWithDFS() {
        // Initialize all cells as walls
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                mazeGrid[row][col] = true; 
            }
        }

        Stack<Point> stack = new Stack<>();
        Point current = startPoint;
        mazeGrid[current.x][current.y] = false; 
        stack.push(current);
        Random rand = new Random();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!stack.isEmpty()) {
            current = stack.peek();
            int x = current.x;
            int y = current.y;

            java.util.List<Point> neighbors = new ArrayList<>();
            for (int[] dir : directions) {
                int nx = x + dir[0] * 2; 
                int ny = y + dir[1] * 2;
                if (nx >= 0 && nx < height && ny >= 0 && ny < width && mazeGrid[nx][ny]) {
                    neighbors.add(new Point(nx, ny));
                }
            }

            if (!neighbors.isEmpty()) {
                Point next = neighbors.get(rand.nextInt(neighbors.size()));

                int wallX = (current.x + next.x) / 2;
                int wallY = (current.y + next.y) / 2;
                mazeGrid[wallX][wallY] = false; 
                mazeGrid[next.x][next.y] = false; 

                stack.push(next); 
            } else {
                stack.pop(); 
            }
        }

        mazeGrid[startPoint.x][startPoint.y] = false;
        mazeGrid[endPoint.x][endPoint.y] = false;

        drawMaze(); // Draw the maze using StdDraw
    }

    // Method to reset maze for regeneration
    public void resetMaze() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                mazeGrid[row][col] = false; 
            }
        }
        startPoint = new Point(0, 0);
        endPoint = new Point(height - 1, width - 1);
        generateMaze("DFS");
    }

    // Method to initialize StdDraw
    private void initStdDraw() {
        StdDraw.setCanvasSize(width * 20, height * 20);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.setPenRadius(0.005);
        StdDraw.enableDoubleBuffering();
    }

    // Method to draw the maze using StdDraw
    private void drawMaze() {
        StdDraw.clear(StdDraw.WHITE);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (mazeGrid[i][j]) {
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
                }
            }
        }

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledCircle(0.5, height - 0.5, 0.3);

        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(width - 0.5, 0.5, 0.3);

        StdDraw.show();
    }
}
