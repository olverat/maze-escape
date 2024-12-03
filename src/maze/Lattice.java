package maze;

import java.awt.Point;

/**
 * Represents a single cell in a maze.
 * 
 * @author Tien Tran
 * @author Tomas Olvera
 */
class Lattice {
    private boolean passable; // Indicates whether the cell is passable
    private Point father; // Stores the coordinates of the parent cell in a path

    /**
     * Constructs a new Lattice object, initially not passable and with no parent.
     */
    public Lattice() {
        this.passable = false;
        this.father = null;
    }

    /**
     * Checks if the cell is passable.
     *
     * @return True if the cell is passable, false otherwise.
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Sets the passable status of the cell.
     *
     * @param passable True if the cell should be passable, false otherwise.
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Gets the coordinates of the parent cell in a path.
     *
     * @return The coordinates of the parent cell, or null if there is no parent.
     */
    public Point getFather() {
        return father;
    }

    /**
     * Sets the coordinates of the parent cell in a path.
     *
     * @param father The coordinates of the parent cell.
     */
    public void setFather(Point father) {
        this.father = father;
    }
}
