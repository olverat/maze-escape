package maze;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * This abstract class provides a blueprint for maze-solving algorithms.
 * 
 * @author Tomas Olvera
 * @author Tien Tran
 */
abstract class AbstractSolveMaze {
    protected Stack<Point> pathStack = null; // Stack to store the solution path

    /**
     * Checks if a given point is outside the boundaries of the maze.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return True if the point is out of bounds, false otherwise.
     */
    protected boolean isOutofBorder(int x, int y, int colNumber, int rowNumber) {
        if ((x == 0 && y == 1) || (x == colNumber + 1 && y == rowNumber))
            return false;
        else
            return (x > colNumber || y > rowNumber || x < 1 || y < 1);
    }

    /**
     * Abstract method to be implemented by concrete maze-solving algorithms.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param entrance The entrance point of the maze.
     * @param exit The exit point of the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return A stack of points representing the solution path.
     */
    abstract Stack<Point> solveMaze(Lattice[][] mazeLattice, Point entrance, Point exit,
            int colNumber, int rowNumber);
}


/**
 * This class implements the Depth-First Search algorithm for maze solving.
 */
class DepthFirstSearchSolveMaze extends AbstractSolveMaze {

    /**
     * Finds an unvisited neighbor of a given point in the maze using Depth-First Search.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param p The current point.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return The coordinates of the unvisited neighbor, or null if none found.
     */
    protected Point aroundPointDepthFirst(Lattice[][] mazeLattice, Point p, int colNumber,
            int rowNumber) {
        final int[] aroundPoint = {-1, 0, 1, 0, -1}; // Relative coordinates of neighbors
        for (int i = 0; i < 4;) {
            int x = p.x + aroundPoint[i];
            int y = p.y + aroundPoint[++i];
            if (!isOutofBorder(x, y, colNumber, rowNumber) && mazeLattice[y][x].isPassable()
                    && mazeLattice[y][x].getFather() == null) {
                mazeLattice[y][x].setFather(p);
                return new Point(x, y);
            }
        }
        return null;
    }

    /**
     * Solves the maze using the Depth-First Search algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param entrance The entrance point of the maze.
     * @param exit The exit point of the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return A stack of points representing the solution path.
     */
    @Override
    public Stack<Point> solveMaze(Lattice[][] mazeLattice, Point entrance, Point exit,
            int colNumber, int rowNumber) {
        pathStack = new Stack<>();
        Deque<Point> pathDeque = new ArrayDeque<>();
        Point judge = new Point(0, 0);
        Point end = new Point(exit.x - 1, exit.y);

        // Reset father pointers
        for (int i = 0; i < rowNumber + 2; ++i)
            for (int j = 0; j < colNumber + 2; ++j)
                mazeLattice[i][j].setFather(null);

        mazeLattice[entrance.y][entrance.x].setFather(judge);
        pathDeque.addLast(entrance);
        Point currentPoint = entrance;

        // Traverse the maze using Depth-First Search
        while (!currentPoint.equals(end)) {
            currentPoint = aroundPointDepthFirst(mazeLattice, currentPoint, colNumber, rowNumber);
            if (currentPoint == null) {
                pathDeque.removeLast();
                if (pathDeque.isEmpty())
                    break;
                currentPoint = pathDeque.getLast();
            } else {
                pathDeque.addLast(currentPoint);
            }
        }

        mazeLattice[exit.y][exit.x].setFather(end);
        pathDeque.addLast(exit);

        // Construct the path stack from the deque
        while (!pathDeque.isEmpty())
            pathStack.push(pathDeque.removeLast());

        return pathStack;
    }
}


/**
 * This class implements the Breadth-First Search algorithm for maze solving.
 */
class BreadthFirstSearchSolveMaze extends AbstractSolveMaze {

    /**
     * Finds all unvisited neighbors of a given point in the maze using Breadth-First Search.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param p The current point.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return An array of points representing the unvisited neighbors.
     */
    protected Point[] aroundPointBreadthFirst(Lattice[][] mazeLattice, Point p, int colNumber,
            int rowNumber) {
        final int[] aroundPoint = {-1, 0, 1, 0, -1}; // Relative coordinates of neighbors
        Point[] point = {null, null, null, null};
        for (int i = 0; i < 4; ++i) {
            int x = p.x + aroundPoint[i];
            int y = p.y + aroundPoint[i + 1];
            if (!isOutofBorder(x, y, colNumber, rowNumber) && mazeLattice[y][x].isPassable()
                    && mazeLattice[y][x].getFather() == null) {
                point[i] = new Point(x, y);
                mazeLattice[y][x].setFather(p);
            }
        }
        return point;
    }

    /**
     * Solves the maze using the Breadth-First Search algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param entrance The entrance point of the maze.
     * @param exit The exit point of the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return A stack of points representing the solution path.
     */
    @Override
    public Stack<Point> solveMaze(Lattice[][] mazeLattice, Point entrance, Point exit,
            int colNumber, int rowNumber) {
        pathStack = new Stack<>();
        Point judge = new Point(0, 0);
        Deque<Point> pathDeque = new ArrayDeque<>();
        Point end = new Point(exit.x - 1, exit.y);

        // Reset father pointers
        for (int i = 0; i < rowNumber + 2; ++i)
            for (int j = 0; j < colNumber + 2; ++j)
                mazeLattice[i][j].setFather(null);

        mazeLattice[entrance.y][entrance.x].setFather(judge);
        pathDeque.addLast(entrance);
        Point currentPoint = entrance;

        // Traverse the maze using Breadth-First Search
        while (!currentPoint.equals(end)) {
            Point[] p = aroundPointBreadthFirst(mazeLattice, currentPoint, colNumber, rowNumber);
            int count = 0; // Count of valid neighbors
            for (Point value : p)
                if (value != null) {
                    pathDeque.addLast(value);
                    ++count;
                }
            if (count == 0) {
                pathDeque.removeLast();
                if (pathDeque.isEmpty())
                    break;
                currentPoint = pathDeque.getLast();
            } else {
                pathDeque.addLast(currentPoint);
            }
        }

        mazeLattice[exit.y][exit.x].setFather(end);

        // Construct the path stack by backtracking from the exit
        for (currentPoint = exit; !currentPoint.equals(judge); currentPoint =
                mazeLattice[currentPoint.y][currentPoint.x].getFather()) {
            pathStack.push(currentPoint);
        }

        return pathStack;
    }
}
