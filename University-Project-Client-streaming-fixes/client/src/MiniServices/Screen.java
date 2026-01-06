package MiniServices;

import javax.swing.*;
import java.awt.*;

public class Screen {
    private static JFrame frame;
    private static JLabel imageLabel;

    private Screen() {
    }

    public static void turnOnScreen() {
        // Assuming "background.jpg" is the image file you want to use
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\Saif\\Desktop\\University-Project\\client\\wait.jpg");

        // Initialize imageLabel with the background image
        imageLabel = new JLabel(backgroundImage);

        frame = new JFrame("Multicast Image Receiver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); // Make the JFrame undecorated

        // Set imageLabel as the content pane
        frame.setContentPane(imageLabel);

        // Set the frame to full screen
        setFullScreen(frame);

        frame.setVisible(true);
    }

    private static void setFullScreen(JFrame frame) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            frame.setResizable(false);
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);
        } else {
            // If full-screen mode is not supported, set the size to maximum screen size
            frame.setSize(new Dimension(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()));
        }
    }
    public static void turnOffScreen() {
        if (frame != null) {
            frame.dispose();
        }
    }
}
