package maze;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * This class extends JTextField to create a timer display that updates every second.
 */
public class Timers extends JTextField {
    private Date now; // Current time
    private Timer timer; // Timer to update the display

    /**
     * Constructs a new Timers object, initializing the timer and setting the initial time to 0.
     */
    public Timers() {
        setEditable(false);
        Calendar time = Calendar.getInstance();
        time.set(0, 0, 0, 0, 0, 0); // Set initial time to 0
        now = time.getTime();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now = new Date(now.getTime() + 1000); // Increment time by 1 second
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); // Format the time
                setText(formatter.format(now)); // Update the display
            }
        });
    }

    /**
     * Starts the timer.
     */
    public void start() {
        timer.start();
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * Restarts the timer.
     */
    public void proceed() {
        timer.restart();
    }

    /**
     * Resets the timer to 0 and restarts it.
     */
    public void restart() {
        stop();
        Calendar time = Calendar.getInstance();
        time.set(0, 0, 0, 0, 0, 0); // Reset time to 0
        now = time.getTime();
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                now = new Date(now.getTime() + 1000); // Increment time by 1 second
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss"); // Format the time
                setText(formatter.format(now)); // Update the display
            }
        });
    }
}
