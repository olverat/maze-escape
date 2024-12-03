package maze;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * This abstract class provides a blueprint for maze creation algorithms.
 * 
 * @author Tien Tran
 * @author Tomas Olvera
 */
abstract class AbstractCreateMaze {

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
     * Abstract method to be implemented by concrete maze creation algorithms.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     */
    abstract void createMaze(Lattice[][] mazeLattice, int colNumber, int rowNumber);
}


/**
 * This class implements the Depth-First Search algorithm for maze creation.
 */
class DepthFirstSearchCreateMaze extends AbstractCreateMaze {

    /**
     * Finds a random unvisited neighbor of a given point in the maze.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param p The current point.
     * @param s The stack used to track the path.
     * @param rand The random number generator.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     * @return The coordinates of the unvisited neighbor, or null if none found.
     */
    protected Point ArroundPoint(Lattice[][] mazeLattice, Point p, Stack<Point> s, Random rand,
            int colNumber, int rowNumber) {
        final int[] arroundPoint = {-2, 0, 2, 0, -2}; // Relative coordinates of neighbors
        int r = rand.nextInt(4);
        for (int i = 0; i < 4; ++i) {
            int j = r % 4;
            int x = p.x + arroundPoint[j];
            int y = p.y + arroundPoint[j + 1];
            ++r;
            if (!isOutofBorder(x, y, colNumber, rowNumber) && !mazeLattice[y][x].isPassable()) {
                mazeLattice[y][x].setPassable(true);
                mazeLattice[p.y + arroundPoint[j + 1] / 2][p.x + arroundPoint[j] / 2]
                        .setPassable(true);
                return new Point(x, y);
            }
        }
        return null;
    }

    /**
     * Creates a maze using the Depth-First Search algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     */
    @Override
    public void createMaze(Lattice[][] mazeLattice, int colNumber, int rowNumber) {
        Random rand = new Random();
        Point currentPoint =
                new Point(2 * rand.nextInt(colNumber / 2) + 1, 2 * rand.nextInt(rowNumber / 2) + 1);
        mazeLattice[currentPoint.y][currentPoint.x].setPassable(true);
        Stack<Point> pathStack = new Stack<>();
        pathStack.push(currentPoint);
        currentPoint =
                ArroundPoint(mazeLattice, currentPoint, pathStack, rand, colNumber, rowNumber);
        while (true) {
            Point p =
                    ArroundPoint(mazeLattice, currentPoint, pathStack, rand, colNumber, rowNumber);
            if (p != null) {
                pathStack.push(currentPoint);
                currentPoint = p;
            } else if (!pathStack.isEmpty())
                currentPoint = pathStack.pop();
            else
                break;
        }
    }
}


/**
 * This class implements the Randomized Prim's algorithm for maze creation.
 */
class RandomizedPrimCreateMaze extends AbstractCreateMaze {

    /**
     * Adds the walls surrounding a given point to a list.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param p The current point.
     * @param list The list to store the walls.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     */
    protected void pushArroundWallToList(Lattice[][] mazeLattice, Point p, List<Point> list,
            int colNumber, int rowNumber) {
        final int[] arroundWall = {-1, 0, 1, 0, -1}; // Relative coordinates of surrounding walls
        for (int i = 0; i < 4;) {
            int x = p.x + arroundWall[i];
            int y = p.y + arroundWall[++i];
            if (!isOutofBorder(x, y, colNumber, rowNumber) && !mazeLattice[y][x].isPassable()) {
                list.add(new Point(x, y));
            }
        }
    }

    /**
     * Finds an unvisited cell connected to a given wall.
     *
     * @param wall The coordinates of the wall.
     * @param mazeLattice The 2D array representing the maze.
     * @return The coordinates of the unvisited cell, or null if none found.
     */
    protected Point findPoint(Point wall, Lattice[][] mazeLattice) {
        final int[] arroundWall = {-1, 0, 1, 0, -1}; // Relative coordinates of surrounding cells
        Point p = null;
        for (int i = (wall.y + 1) % 2; i < 2; i += 2) {
            boolean add =
                    mazeLattice[wall.y + arroundWall[i + 1]][wall.x + arroundWall[i]].isPassable(),
                    sub = mazeLattice[wall.y - arroundWall[i + 1]][wall.x - arroundWall[i]]
                            .isPassable();
            if (add && !sub) {
                p = new Point(wall.x - arroundWall[i], wall.y - arroundWall[i + 1]);
                break;
            }
            if (!add && sub) {
                p = new Point(wall.x + arroundWall[i], wall.y + arroundWall[i + 1]);
                break;
            }
        }
        return p;
    }

    /**
     * Creates a maze using the Randomized Prim's algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     */
    @Override
    public void createMaze(Lattice[][] mazeLattice, int colNumber, int rowNumber) {
        Random rand = new Random();
        Point currentPoint =
                new Point(2 * rand.nextInt(colNumber / 2) + 1, 2 * rand.nextInt(rowNumber / 2) + 1);
        mazeLattice[currentPoint.y][currentPoint.x].setPassable(true);
        List<Point> listWall = new LinkedList<>();
        pushArroundWallToList(mazeLattice, currentPoint, listWall, colNumber, rowNumber);
        while (!listWall.isEmpty()) {
            int k = rand.nextInt(listWall.size());
            Point wall = listWall.remove(k);
            currentPoint = findPoint(wall, mazeLattice);
            if (currentPoint != null) {
                mazeLattice[wall.y][wall.x].setPassable(true);
                mazeLattice[currentPoint.y][currentPoint.x].setPassable(true);
                pushArroundWallToList(mazeLattice, currentPoint, listWall, colNumber, rowNumber);
            }
        }
    }
}


/**
 * This class implements the Recursive Division algorithm for maze creation.
 */
class RecursiveDivisionCreateMaze extends AbstractCreateMaze {

    /**
     * Opens a door in a wall between two points.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param p1 The first point.
     * @param p2 The second point.
     * @param r The random number generator.
     */
    protected void openAdoor(Lattice[][] mazeLattice, Point p1, Point p2, Random r) {
        if (p1.y == p2.y && p1.x == p2.x) {
            mazeLattice[p1.y][p1.x].setPassable(true);
            return;
        }
        if (p1.y == p2.y) {
            int pos = p1.x + r.nextInt((p2.x - p1.x) / 2) * 2; // Ensure the position is odd
            mazeLattice[p1.y][pos].setPassable(true);
        } else if (p1.x == p2.x) {
            int pos = p1.y + r.nextInt((p2.y - p1.y) / 2) * 2; // Ensure the position is odd
            mazeLattice[pos][p1.x].setPassable(true);
        }
    }

    /**
     * Recursively creates a maze using the Recursive Division algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param start The starting point of the current section.
     * @param height The height of the current section.
     * @param width The width of the current section.
     * @param rand The random number generator.
     */
    private void recursiveCreateMaze(Lattice[][] mazeLattice, Point start, int height, int width,
            Random rand) {
        if (height <= 2 || width <= 2)
            return;
        // Draw a horizontal wall
        int drawx = start.y + rand.nextInt(height / 2) * 2 + 1; // Ensure the position is odd
        for (int i = start.x; i < start.x + width; ++i)
            mazeLattice[drawx][i].setPassable(false);
        // Draw a vertical wall
        int drawy = start.x + rand.nextInt(width / 2) * 2 + 1; // Ensure the position is odd
        for (int i = start.y; i < start.y + height; ++i)
            mazeLattice[i][drawy].setPassable(false);
        // Open a door in one of the four walls
        int opendoor = rand.nextInt(4) + 1;
        switch (opendoor) {
            case 1:
                openAdoor(mazeLattice, new Point(drawy, drawx + 1),
                        new Point(drawy, start.y + height - 1), rand);// 2
                openAdoor(mazeLattice, new Point(drawy + 1, drawx),
                        new Point(start.x + width - 1, drawx), rand);// 3
                openAdoor(mazeLattice, new Point(drawy, start.y), new Point(drawy, drawx - 1),
                        rand);// 4
                break;
            case 2:
                openAdoor(mazeLattice, new Point(drawy + 1, drawx),
                        new Point(start.x + width - 1, drawx), rand);// 3
                openAdoor(mazeLattice, new Point(drawy, start.y), new Point(drawy, drawx - 1),
                        rand);// 4
                openAdoor(mazeLattice, new Point(start.x, drawx), new Point(drawy - 1, drawx),
                        rand);// 1
                break;
            case 3:
                openAdoor(mazeLattice, new Point(drawy, start.y), new Point(drawy, drawx - 1),
                        rand);// 4
                openAdoor(mazeLattice, new Point(start.x, drawx), new Point(drawy - 1, drawx),
                        rand);// 1
                openAdoor(mazeLattice, new Point(drawy, drawx + 1),
                        new Point(drawy, start.y + height - 1), rand);// 2
                break;
            case 4:
                openAdoor(mazeLattice, new Point(start.x, drawx), new Point(drawy - 1, drawx),
                        rand);// 1
                openAdoor(mazeLattice, new Point(drawy, drawx + 1),
                        new Point(drawy, start.y + height - 1), rand);// 2
                openAdoor(mazeLattice, new Point(drawy + 1, drawx),
                        new Point(start.x + width - 1, drawx), rand);// 3
                break;
        }
        // Recursively create mazes in the four sections
        recursiveCreateMaze(mazeLattice, start, drawx - start.y, drawy - start.x, rand);
        recursiveCreateMaze(mazeLattice, new Point(drawy + 1, start.y), drawx - start.y,
                width - drawy + start.x - 1, rand);
        recursiveCreateMaze(mazeLattice, new Point(start.x, drawx + 1),
                height - drawx + start.y - 1, drawy - start.x, rand);
        recursiveCreateMaze(mazeLattice, new Point(drawy + 1, drawx + 1),
                height - drawx + start.y - 1, width - drawy + start.x - 1, rand);
    }

    /**
     * Creates a maze using the Recursive Division algorithm.
     *
     * @param mazeLattice The 2D array representing the maze.
     * @param colNumber The number of columns in the maze.
     * @param rowNumber The number of rows in the maze.
     */
    @Override
    public void createMaze(Lattice[][] mazeLattice, int colNumber, int rowNumber) {
        Random rand = new Random();
        for (int i = 1; i < colNumber + 1; ++i)
            for (int j = 1; j < rowNumber + 1; ++j)
                mazeLattice[j][i].setPassable(true);
        Point start = new Point(1, 1);
        recursiveCreateMaze(mazeLattice, start, rowNumber, colNumber, rand);
    }
}
