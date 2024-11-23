package Maze;

import java.awt.Point;
import java.util.*;
import edu.princeton.cs.algs4.Stack;

public class MazeSolver {

    public List<Point> solve(Maze maze, String algorithm) {
        List<Point> path = new ArrayList<>();

        if (algorithm.equals("DFS")) {
            path = solveWithDFS(maze);
        } else if (algorithm.equals("BFS")) {
            // Solve using BFS (You can add the implementation later)
        }
        return path;
    }

    private List<Point> solveWithDFS(Maze maze) {
        Stack<Point> stack = new Stack<>();
        Set<Point> visited = new HashSet<>(); 
        Map<Point, Point> parentMap = new HashMap<>(); // To reconstruct the path

        Point start = maze.getStartPoint();
        Point end = maze.getEndPoint();

        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Point current = stack.pop();

            if (current.equals(end)) {
                return reconstructPath(parentMap, end); // Solution found
            }

            // Explore adjacent cells (up, down, left, right)
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                Point neighbor = new Point(nx, ny);

                // Check if the neighbor is valid and not visited
                if (nx >= 0 && nx < maze.getHeight() && 
                    ny >= 0 && ny < maze.getWidth() && 
                    !maze.isWall(neighbor) && !visited.contains(neighbor)) {

                    stack.push(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, current); // Store parent for path reconstruction
                }
            }
        }

        return null; // No solution found
    }

    // Helper function to reconstruct the path from the parent map
    private List<Point> reconstructPath(Map<Point, Point> parentMap, Point end) {
        List<Point> path = new ArrayList<>();
        Point current = end;
        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }
        Collections.reverse(path); // Reverse to get the path from start to end
        return path;
    }
}
