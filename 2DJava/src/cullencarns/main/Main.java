package cullencarns.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Controls all aspects of the program
 *
 * @author Cullen Carns
 */
public class Main {

    //////////////// WINDOW VARIABLES -->
    /* Main window of the program  */
    private JFrame window;
    /* Title of the window, has no effect if window is undecorated or in full
     screen */
    private final String title = "2D";
    /* If true, the window is full screen */
    private boolean fullscreen = false;
    /* If true, F11 toggles fullscreen */
    private final boolean fullscreenToggleable = true;
    /* Whether or not the window has a border, min, max, and exit buttons, etc.
     Overriden if window is fullScreen */
    private final boolean decorated = true;
    /* Whether or not window is resizeable */
    private final boolean resizeable = true;

    /* Tells min and max values of window dimensions, if <0 (i.e. -1) it is
     ignored, also ignored if resizeable is false
     THIS IS PLATFORM DEPENDENT!! I have used a work around by updating the
     window, but this doesn't look as nice. Users will still get the idea when
     they cannot resize the window  */
    private final int minWinWidth = -1, minWinHeight = -1, maxWinWidth = -1,
            maxWinHeight = -1;

    /* Window coords, if <0 (i.e. -1) window is centered on screen */
    private int winX = -1, winY = -1;
    /* Overriden if canvas width and height are >0 */
    private int winWidth = 980, winHeight = 720;
    //////////////// <-- WINDOW VARIABLES

    //////////////// CANVAS AND PANEL VARIABLES -->
    private Canvas canvas;
    private BufferStrategy bs;
    private JPanel panel;
    /* Tells min and max values of canvas dimensions, if <0 (i.e. -1) it is
     ignored, also ignored if resizeable is false, overrides window size
     THIS IS PLATFORM DEPENDENT!! I have used a work around by updating the
     window, but this doesn't look as nice. Users will still get the idea when
     they cannot resize the window  */
    private final int minCanvasWidth = -1, minCanvasHeight = -1, maxCanvasWidth = -1,
            maxCanvasHeight = -1;
    /* Canvas size adjusts automatically to window, if canvas width and height
     are set >0 here, window adjusts to match canvas size (the window borders
     have a thickness and that is incorporated into window width and height,
     setting canvas size ensures draw area is a specific size) overriden by
     fullscreen */
    private int canvasWidth = 980, canvasHeight = 720;
    /* Number of buffers in buffer strategy */
    private int numBuffers = 2;
    //////////////// <-- CANVAS AND PANEL VARIABLES

    //////////////// GAME LOGIC VARIABLES -->
    private boolean isRunning = true;
    private double delta;
    private double lastUpdateTime;
    //////////////// <-- GAME LOGIC VARIABLES

    public static void main(String[] args) {
        /* Create a new Main and call the start function */
        new Main().start();
    }

    public void start() {
        initWindow();
        updateLoop();
    }

    private void updateLoop() {
        /* If last update time started at 0, the first delta would be huge */
        lastUpdateTime = System.nanoTime();
        while (isRunning) {
            updateWindow();

            /* Update time variables */
            double now = System.nanoTime();
            delta = (now - lastUpdateTime) / 1000000000;
            lastUpdateTime = now;

            update();

            /* Make sure no contents are lost during rendering */
            do {
                render();
            } while (bs.contentsLost());

            /* Make sure some time passes between updates */
            while (!(System.nanoTime() > now)) {
                for (int i = 0; i < 1000; i++) {
                }
            }
        }
    }

    private void update() {
        /* Update stuff */
    }

    private void render() {
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        /* Draw background */
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, canvasWidth, canvasHeight);

        /* Draw more things */

        /* Show buffer strategy */
        g.dispose();
        bs.show();
    }

    private void initWindow() {
        int width = this.winWidth;
        int height = this.winHeight;
        window = new JFrame(title);

        /* Apply fullscreen and decorations to window */
        if (fullscreen) {
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

        /* If true, canvas size overrides window size */
        boolean canvasSizeOverride = !fullscreen && canvasWidth > 0 && canvasHeight > 0;
        canvas = new Canvas();
        panel = (JPanel) window.getContentPane();
        /* Set canvas size, make it too big if it doesn't override window that
         way it gets updated after setting the window size */
        if (canvasSizeOverride) {
            canvas.setBounds(0, 0, canvasWidth, canvasHeight);
        } else {
            canvas.setBounds(0, 0, winWidth, winHeight);
        }
        /* Set ignore repaint because we will be painting canvas manually */
        canvas.setIgnoreRepaint(true);
        panel.add(canvas);
        /* Size window appropriately */
        if (canvasSizeOverride) {
            window.pack();
        } else {
            window.setSize(winWidth, winHeight);
        }

        /* Apply position to window */
        if (winX >= 0 && winY >= 0) {
            window.setLocation(winX, winY);
        } else {
            window.setLocationRelativeTo(null);
        }
        /* Apply resize features to window */
        if (resizeable) {
            if (minWinWidth >= 0 && minWinHeight >= 0) {
                window.setMinimumSize(new Dimension(minWinWidth, minWinHeight));
            }
            if (maxWinWidth >= 0 && maxWinHeight >= 0) {
                window.setMaximumSize(new Dimension(maxWinWidth, maxWinHeight));
            }
        }
        window.setResizable(resizeable);

        /* Make program exit when window closes */
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* Update canvas width and height */
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        /* Open the window */
        window.setVisible(true);

        /* Create buffer strategy */
        canvas.createBufferStrategy(numBuffers);
        bs = canvas.getBufferStrategy();
    }

    private void updateWindow() {
        if (!fullscreen) {
            /* Ensure the window obeys the min and max values */
            winWidth = window.getWidth();
            winHeight = window.getHeight();
            canvasWidth = canvas.getWidth();
            canvasHeight = canvas.getHeight();
            winX = window.getX();
            winY = window.getY();
            boolean canvasMinOverride = minCanvasWidth >= 0 && minCanvasHeight >= 0;
            boolean canvasMaxOverride = maxCanvasWidth >= 0 && maxCanvasHeight >= 0;
            if (canvasMinOverride) {
                if (canvasWidth < minCanvasWidth) {
                    canvasWidth = minCanvasWidth;
                }
                if (canvasHeight < minCanvasHeight) {
                    canvasHeight = minCanvasHeight;
                }
                canvas.setSize(canvasWidth, canvasHeight);
                window.pack();
            } else {
                if (minWinWidth >= 0 && winWidth < minWinWidth) {
                    winWidth = minWinWidth;
                }
                if (minWinHeight >= 0 && winHeight < minWinHeight) {
                    winHeight = minWinHeight;
                }
                window.setSize(winWidth, winHeight);
            }
            if (canvasMaxOverride) {
                if (canvasWidth > maxCanvasWidth) {
                    canvasWidth = maxCanvasWidth;
                }
                if (canvasHeight > maxCanvasHeight) {
                    canvasHeight = maxCanvasHeight;
                }
                canvas.setSize(canvasWidth,canvasHeight);
                window.pack();
            } else {
                if (maxWinWidth >= 0 && winWidth > maxWinWidth) {
                    winWidth = maxWinWidth;
                }
                if (maxWinHeight >= 0 && winHeight > maxWinHeight) {
                    winHeight = maxWinHeight;
                }
                window.setSize(winWidth, winHeight);
            }
        }
    }
}
