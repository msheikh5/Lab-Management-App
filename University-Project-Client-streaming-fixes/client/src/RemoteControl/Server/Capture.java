package RemoteControl.Server;

import Services.Handler;
import org.xerial.snappy.Snappy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Capture implements Runnable{
    private static boolean isRunning=false;
    public static void isRunning(boolean flag){
        isRunning=flag;
    }
    @Override
    public void run() {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        isRunning=true;
        Rectangle capture = new Rectangle(1920, 1080);
        while (isRunning) {
            //System.out.println("capture is running");
            BufferedImage image = robot.createScreenCapture(capture);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpeg", baos);
                byte[] fullBuffer = baos.toByteArray();
                byte[] compressed= new byte[0];
                compressed = Snappy.compress(fullBuffer);
                Handler.baos.add(compressed);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
