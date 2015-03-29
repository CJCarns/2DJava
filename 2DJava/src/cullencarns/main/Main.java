package cullencarns.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Cullen Carns
 */
public class Main {

    ////////////////WINDOW VARIABLES -->
    
    // Main window of the program
    private JFrame window;
    // Title of the window, has no effect if window is undecorated or in full
    // screen
    private final String title = "2D";
    // If true, the window is full screen
    private boolean fullScreen = false;
    // If true, F11 toggles fullscreen
    private boolean fullScreenToggleable = true;
    // Whether or not the window has a border, min, max, and exit buttons, etc.
    // Overriden if window is fullScreen
    private final boolean decorated = true;
    // Whether or not window is resizeable
    private final boolean resizeable = true;

    // Tells min and max values of window dimensions, if <0 (i.e. -1) it is
    // ignored, also ignored if resizeable is false;
    // THIS IS PLATFORM DEPENDENT!! I have used a work around by updating the
    // window, but this doesn't look as nice. Users will still get the idea when
    // they cannot resize the window
    private final int minWidth = -1, minHeight = -1, maxWidth = -1, 
            maxHeight = -1;

    // Window coords, if <0 (i.e. -1) window is centered on screen
    private int winX = -1, winY = -1;
    private int width = 980, height = 720;
    
    //////////////// <-- WINDOW VARIABLES
    
    ////////////////

    public static void main(String[] args) {
        // Create a new Main and call the start function (this gets rid of the
        // need for statics)
        new Main().start();
    }

    public void start() {
        initWindow();

        while (true) {
            updateWindow();
        }
    }

    private void initWindow() {
        window = new JFrame(title);

        // Apply fullscreen and decorations to window
        if (fullScreen) {
            try {
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                width = (int) dim.getWidth();
                height = (int) dim.getHeight();
            } catch (Exception e) {
                System.out.println(e);
            }
            window.setUndecorated(true);
        } else {
            window.setUndecorated(!decorated);
        }
        // Apply bounds and position
        if (winX >= 0 && winY >= 0) {
            window.setBounds(winX, winY, width, height);
        } else {
            window.setSize(width, height);
            window.setLocationRelativeTo(null);
        }
        // Apply resize features to window
        if (resizeable) {
            if (minWidth >= 0 && minHeight >= 0) {
                window.setMinimumSize(new Dimension(minWidth, minHeight));
            }
            if (maxWidth >= 0 && maxHeight >= 0) {
                window.setMaximumSize(new Dimension(maxWidth, maxHeight));
            }
        }
        window.setResizable(resizeable);

        // Make program exit when window closes
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Open the window
        window.setVisible(true);
    }

    private void updateWindow() {
        // Ensure the window obeys the min and max values
        width = window.getWidth();
        height = window.getHeight();
        winX = window.getX();
        winY = window.getY();
        if (maxWidth >= 0 && window.getWidth() > maxWidth) {
            width = maxWidth;
        } else if (minWidth >= 0 && window.getWidth() < minWidth) {
            width = minWidth;
        }
        if (maxHeight >= 0 && window.getHeight() > maxHeight) {
            height = maxHeight;
        } else if (minHeight >= 0 && window.getHeight() < minHeight) {
            height = minHeight;
        }
        window.setSize(width, height);
    }
}
