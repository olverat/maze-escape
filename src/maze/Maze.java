package maze;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * This class represents the maze game panel. It handles maze creation, user interaction, and game
 * logic.
 * 
 * @author Tomas Olvera
 * @author Tien Tran
 */
class Maze extends JPanel {
    private Point entrance; // Entrance coordinates
    private Point exit; // Exit coordinates
    private int rowNumber; // Number of rows in the maze
    private int colNumber; // Number of columns in the maze
    private int latticeWidth; // Width of each cell in the maze
    private Ball ball; // The ball object that the user controls
    private Lattice[][] mazeLattice; // 2D array representing the maze
    private boolean startTiming; // Flag to indicate if the timer is running
    private JPanel panel = new JPanel(); // Panel to hold the timer and step counter
    private JTextField timeText = new Timers(); // Timer display
    private JTextField stepNumberText = new JTextField("0"); // Step counter display
    private boolean computerDo; // Flag to indicate if the computer is solving the maze
    private Thread thread; // Thread for computer solving
    private int stepNumber; // Number of steps taken by the user
    private static final char DEPTH_FIRST_SEARCH_SOLVE_MAZE = 0; // Constant for Depth-First Search
                                                                 // solving
    private static final char BREADTH_FIRST_SEARCH_SOLVE_MAZE = 1; // Constant for Breadth-First
                                                                   // Search solving
    private char solveMaze = DEPTH_FIRST_SEARCH_SOLVE_MAZE; // Current solving algorithm
    private static final char DEPTH_FIRST_SEARCH_CREATE_MAZE = 0; // Constant for Depth-First Search
                                                                  // creation
    private static final char RANDOMIZED_PRIM_CREATE_MAZE = 1; // Constant for Randomized Prim's
                                                               // creation
    private static final char RECURSIVE_DIVISION_CREATE_MAZE = 2; // Constant for Recursive Division
                                                                  // creation
    private char mazeGenerationMethod = DEPTH_FIRST_SEARCH_CREATE_MAZE; // Current creation
                                                                        // algorithm
    private boolean showSolutionPath; // Flag to indicate if the solution path should be displayed

    /**
     * Constructs a new Maze object with the specified number of rows and columns.
     *
     * @param row The number of rows in the maze.
     * @param col The number of columns in the maze.
     */
    public Maze(int row, int col) {
        this.setRowNumber(row);
        this.setColNumber(col);
        this.latticeWidth = 15;
        mazeLattice = new Lattice[getRowNumber() + 2][getColNumber() + 2];
        setLayout(new BorderLayout(0, 0));

        // Initialize timer display
        getTimeText().setForeground(Color.BLUE);
        getTimeText().setFont(new Font("Dialog", Font.PLAIN, 14));
        getTimeText().setHorizontalAlignment(JTextField.CENTER);

        // Initialize step counter display
        getStepNumberText().setEnabled(false);
        getStepNumberText().setForeground(Color.BLUE);
        getStepNumberText().setFont(new Font("Dialog", Font.PLAIN, 14));
        getStepNumberText().setHorizontalAlignment(JTextField.CENTER);

        // Add labels and text fields to the panel
        Label timeLabel = new Label("Time:");
        Label stepLabel = new Label("Step Number:");
        timeLabel.setAlignment(Label.RIGHT);
        stepLabel.setAlignment(Label.RIGHT);
        panel.setLayout(new GridLayout(1, 4));
        add(panel, BorderLayout.NORTH);
        panel.add(timeLabel);
        panel.add(getTimeText());
        panel.add(stepLabel);
        panel.add(getStepNumberText());
        panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        // Add mouse listener to request focus when clicked
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isComputerDo()) {
                    requestFocus();
                }
            }
        });

        setKeyListener();
        createMaze();
    }

    /**
     * Initializes the maze with a new lattice, ball, entrance, and exit.
     */
    public void init() {
        mazeLattice = new Lattice[getRowNumber() + 2][getColNumber() + 2];
        setShowSolutionPath(false);
        setComputerDo(false);
        setThreadStop();
        resetStepNumber();
        resetTimer();

        // Initialize lattice cells
        for (int i = 1; i < getRowNumber() + 1; ++i)
            for (int j = 1; j < getColNumber() + 1; ++j) {
                mazeLattice[i][j] = new Lattice();
            }

        // Initialize border cells
        for (int i = 0; i < getRowNumber() + 2; ++i) {
            mazeLattice[i][0] = new Lattice();
            mazeLattice[i][getColNumber() + 1] = new Lattice();
        }
        for (int j = 0; j < getColNumber() + 2; ++j) {
            mazeLattice[0][j] = new Lattice();
            mazeLattice[getRowNumber() + 1][j] = new Lattice();
        }

        // Set ball, entrance, and exit
        ball = new Ball(0, 1);
        setEntrance(new Point(0, 1));
        setExit(new Point(getColNumber() + 1, getRowNumber()));
        mazeLattice[getEntrance().y][getEntrance().x].setPassable(true);
        mazeLattice[getExit().y][getExit().x].setPassable(true);
    }

    /**
     * Checks if the ball has reached the exit.
     *
     * @return True if the ball is at the exit, false otherwise.
     */
    public boolean exitReached() {
        return getExit().x == ball.getX() && getExit().y == ball.getY();
    }

    /**
     * Displays a game over message with the time and steps taken.
     */
    private void gameOverMessage() {
        JOptionPane.showMessageDialog(null,
                "Congratulations on getting out of the maze!\n" + "You took " + timeText.getText()
                        + " seconds!" + "\nIt took you " + stepNumber + " steps to solve the maze.",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Checks if a given point is outside the boundaries of the maze.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return True if the point is out of bounds, false otherwise.
     */
    private boolean isOutofBorder(int x, int y) {
        if ((x == 0 && y == 1) || (x == getColNumber() + 1 && y == getRowNumber()))
            return false;
        else
            return (x > getColNumber() || y > getRowNumber() || x < 1 || y < 1);
    }

    /**
     * Paints the maze and the ball on the panel.
     *
     * @param g The Graphics object to paint on.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw maze lattice
        for (int i = 0; i < getRowNumber() + 2; ++i)
            for (int j = 0; j < getColNumber() + 2; ++j) {
                g.drawRect((j + 1) * latticeWidth, (i + 1) * latticeWidth + 30, latticeWidth,
                        latticeWidth);
                g.setColor(mazeLattice[i][j].isPassable() ? Color.WHITE : Color.BLACK);
                g.fillRect((j + 1) * latticeWidth, (i + 1) * latticeWidth + 30, latticeWidth,
                        latticeWidth);
            }

        // Draw exit
        g.setColor(Color.RED);
        g.fillRect((getColNumber() + 2) * latticeWidth, (getRowNumber() + 1) * latticeWidth + 30,
                latticeWidth, latticeWidth);

        // Draw ball
        g.setColor(ball.getColor());
        g.drawOval((ball.getX() + 1) * latticeWidth, (ball.getY() + 1) * latticeWidth + 30,
                latticeWidth, latticeWidth);
        g.fillOval((ball.getX() + 1) * latticeWidth, (ball.getY() + 1) * latticeWidth + 30,
                latticeWidth, latticeWidth);

        // Draw solution path if prompted
        if (isShowSolutionPath()) {
            Stack<Point> pathStack = promptsolveMaze();
            g.setColor(Color.GREEN);
            Point start = pathStack.pop();
            while (!pathStack.isEmpty()) {
                Point end = pathStack.pop();
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3.0f));
                g2.drawLine((int) (start.getX() + 1) * latticeWidth + latticeWidth / 2,
                        (int) (start.getY() + 1) * latticeWidth + 30 + latticeWidth / 2,
                        (int) (end.getX() + 1) * latticeWidth + latticeWidth / 2,
                        (int) (end.getY() + 1) * latticeWidth + 30 + latticeWidth / 2);
                start = end;
            }
        }
    }

    /**
     * Moves the ball in the maze based on user input.
     *
     * @param c The key code of the pressed key.
     */
    synchronized private void move(int c) {
        int tx = ball.getX(), ty = ball.getY();
        switch (c) {
            case KeyEvent.VK_LEFT:
                --tx;
                break;
            case KeyEvent.VK_A:
                --tx;
                break;
            case KeyEvent.VK_RIGHT:
                ++tx;
                break;
            case KeyEvent.VK_D:
                ++tx;
                break;
            case KeyEvent.VK_UP:
                --ty;
                break;
            case KeyEvent.VK_W:
                --ty;
                break;
            case KeyEvent.VK_DOWN:
                ++ty;
                break;
            case KeyEvent.VK_S:
                ++ty;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            default:
                // Ignore invalid keys
                return;
        }

        if (!isOutofBorder(tx, ty) && mazeLattice[ty][tx].isPassable()) {
            ball.setX(tx);
            ball.setY(ty);
            ++stepNumber;
            stepNumberText.setText(Integer.toString(stepNumber));

            if (!isStartTiming()) {
                setStartTiming(true);
                ((Timers) getTimeText()).start();
            }
        }
    }

    /**
     * Sets the key listener for the maze panel to handle user input.
     */
    private void setKeyListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!exitReached()) {
                    int c = e.getKeyCode();
                    move(c);
                    repaint();
                    // if (isWin() && !isComputerDo())
                    // gameOverMessage();
                } else {
                    gameOverMessage();
                }
            }
        });
    }

    /**
     * Resets the timer to 0.
     */
    public void resetTimer() {
        setStartTiming(false);
        getTimeText().setText("00:00:00");
        ((Timers) timeText).restart();
    }

    /**
     * Resets the step counter to 0.
     */
    public void resetStepNumber() {
        setStepNumber(0);
        stepNumberText.setText(Integer.toString(stepNumber));
    }

    /**
     * Sets the position of the ball in the maze.
     *
     * @param p The new coordinates of the ball.
     */
    public void setBallPosition(Point p) {
        ball.setX(p.x);
        ball.setY(p.y);
        repaint();
    }

    /**
     * Creates a new maze using the selected creation algorithm.
     */
    public void createMaze() {
        init();
        AbstractCreateMaze c;
        switch (getMazeGenerationMethod()) {
            case DEPTH_FIRST_SEARCH_CREATE_MAZE:
                c = new DepthFirstSearchCreateMaze();
                break;
            case RANDOMIZED_PRIM_CREATE_MAZE:
                c = new RandomizedPrimCreateMaze();
                break;
            case RECURSIVE_DIVISION_CREATE_MAZE:
                c = new RecursiveDivisionCreateMaze();
                break;
            default:
                return;
        }
        c.createMaze(mazeLattice, getColNumber(), getRowNumber());
        repaint();
    }

    /**
     * Solves the maze from a given point using the selected solving algorithm.
     *
     * @param p The starting point for solving.
     * @return A stack of points representing the solution path.
     */
    private Stack<Point> solveMaze(Point p) {
        AbstractSolveMaze a;
        switch (getSolveMaze()) {
            case BREADTH_FIRST_SEARCH_SOLVE_MAZE:
                a = new BreadthFirstSearchSolveMaze();
                break;
            case DEPTH_FIRST_SEARCH_SOLVE_MAZE:
                a = new DepthFirstSearchSolveMaze();
                break;
            default:
                return null;
        }
        return a.solveMaze(mazeLattice, p, getExit(), getColNumber(), getRowNumber());
    }

    /**
     * Solves the maze from the current ball position using the selected solving algorithm.
     *
     * @return A stack of points representing the solution path.
     */
    private Stack<Point> promptsolveMaze() {
        AbstractSolveMaze a;
        switch (getSolveMaze()) {
            case BREADTH_FIRST_SEARCH_SOLVE_MAZE:
                a = new BreadthFirstSearchSolveMaze();
                break;
            case DEPTH_FIRST_SEARCH_SOLVE_MAZE:
                a = new DepthFirstSearchSolveMaze();
                break;
            default:
                return null;
        }
        return a.solveMaze(mazeLattice, new Point(ball.getX(), ball.getY()), getExit(),
                getColNumber(), getRowNumber());
    }

    /**
     * Shows the solution path for a given time.
     *
     * @param time The time in milliseconds to display the solution path.
     */
    private void computerSolveMazeForBallPositionForTime(int time) {
        if (getThread() == null)
            setThread(new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        setShowSolutionPath(true);
                        repaint();
                        Thread.sleep(time);
                        setShowSolutionPath(false);
                        repaint();
                        setThreadStop();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }));
        getThread().start();
    }

    /**
     * Shows the solution path from the current ball position.
     *
     * @return True if the solution path is displayed, false otherwise.
     */
    public boolean computerSolveMazeForBallPosition() {
        setThreadStop();
        ((Timers) getTimeText()).stop();
        int time;
        Object[] selections = {"forever", "10s", "5s", "3s", "1s"};
        Object select = JOptionPane.showInputDialog(null, "Solution Timer", "Maze Escape",
                JOptionPane.INFORMATION_MESSAGE, null, selections, selections[2]);
        if (select != null) {
            switch ((String) select) {
                case "forever":
                    time = 2000000000;
                    break;
                case "10s":
                    time = 10000;
                    break;
                case "5s":
                    time = 5000;
                    break;
                case "3s":
                    time = 3000;
                    break;
                case "1s":
                    time = 1000;
                    break;
                default:
                    return false;
            }
            computerSolveMazeForBallPositionForTime(time);
            ((Timers) getTimeText()).proceed();
            return true;
        } else
            return false;
    }

    /**
     * Solves the maze from the starting position using the selected solving algorithm and animates
     * the ball moving along the solution path.
     *
     * @param speed The speed of the ball animation in milliseconds per step.
     */
    private void computerSolveMazeForSpeed(int speed) {
        setComputerDo(true);
        Point p = exitReached() ? getEntrance() : new Point(ball.getX(), ball.getY());
        Stack<Point> stack = solveMaze(p);
        resetTimer();
        resetStepNumber();
        if (getThread() == null)
            setThread(new Thread(() -> {
                try {
                    while (!stack.isEmpty() && !Thread.interrupted()) {
                        Point p1 = stack.pop();
                        setBallPosition(p1);
                        ++stepNumber;
                        stepNumberText.setText(Integer.toString(stepNumber));
                        Thread.sleep(speed);
                    }
                } catch (InterruptedException e) {
                    // Thread interrupted
                }
            }));
        getThread().start();
    }

    /**
     * Solves the maze from the starting position and animates the ball moving along the solution
     * path at a user-selected speed.
     *
     * @return True if the maze is solved, false otherwise.
     */
    public boolean computerSolveMaze() {
        int speed;
        setThreadStop();
        Object[] selections =
                {"lower speed", "low speed", "medium speed", "high speed", "higher speed"};
        Object select = JOptionPane.showInputDialog(null,
                "Select the playback speed for this solution:", "Maze Escape",
                JOptionPane.INFORMATION_MESSAGE, null, selections, selections[2]);
        if (select != null) {
            switch ((String) select) {
                case "lower speed":
                    speed = 400;
                    break;
                case "low speed":
                    speed = 300;
                    break;
                case "medium speed":
                    speed = 200;
                    break;
                case "high speed":
                    speed = 100;
                    break;
                case "higher speed":
                    speed = 20;
                    break;
                default:
                    return false;
            }
            computerSolveMazeForSpeed(speed);
            return true;
        } else
            return false;
    }

    // Getters and setters for various properties

    public int getLatticeWidth() {
        return latticeWidth;
    }

    public void setLatticeWidth(int latticeWidth) {
        this.latticeWidth = latticeWidth;
    }

    public JTextField getTimeText() {
        return timeText;
    }

    public boolean isStartTiming() {
        return startTiming;
    }

    public void setStartTiming(boolean startTiming) {
        this.startTiming = startTiming;
    }

    public Point getEntrance() {
        return entrance;
    }

    public void setEntrance(Point entrance) {
        this.entrance = entrance;
    }

    public Point getExit() {
        return exit;
    }

    private void setExit(Point exit) {
        this.exit = exit;
    }

    public boolean isComputerDo() {
        return computerDo;
    }

    public void setComputerDo(boolean computerDo) {
        this.computerDo = computerDo;
    }

    public Thread getThread() {
        return thread;
    }

    private void setThread(Thread thread) {
        this.thread = thread;
    }

    public void setThreadStop() {
        if (getThread() != null) {
            if (isShowSolutionPath())
                setShowSolutionPath(false);
            thread.interrupt();
            setThread(null);
        }
    }

    public JTextField getStepNumberText() {
        return stepNumberText;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public char getSolveMaze() {
        return solveMaze;
    }

    public void setSolveMaze(char solveMaze) {
        this.solveMaze = solveMaze;
    }

    public char getMazeGenerationMethod() {
        return mazeGenerationMethod;
    }

    public void setMazeGenerationMethod(char algorithm) {
        this.mazeGenerationMethod = algorithm;
    }

    public boolean isShowSolutionPath() {
        return showSolutionPath;
    }

    public void setShowSolutionPath(boolean show) {
        this.showSolutionPath = show;
    }
}
