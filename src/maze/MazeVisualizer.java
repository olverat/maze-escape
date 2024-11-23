package maze;

import javax.swing.*;
import java.awt.*;

public class MazeVisualizer extends JFrame {

    public MazeVisualizer() {
        // Set up the window
        setTitle("Maze Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Can change this if needed
        mainPanel.setLayout(new BorderLayout()); // Can change this if needed

        // Add some sample text
        JLabel label = new JLabel("Welcome to the maze visualizer!");
        label.setFont(new Font("Inter", Font.PLAIN, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(label, BorderLayout.CENTER);

        // Add the panel to the frame
        setContentPane(mainPanel);

        // Size and center the window
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Make sure heavyweight components are painted properly
        setBackground(new Color(240, 240, 240));
    }

    public static void main(String[] args) {
        // Ensure Swing operations run on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MazeVisualizer app = new MazeVisualizer();
            app.setVisible(true);
        });
    }
}
