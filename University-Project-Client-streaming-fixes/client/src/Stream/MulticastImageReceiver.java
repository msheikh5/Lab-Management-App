package Stream;

import auxiliaryClasses.IPorts;
import network.*;
import org.xerial.snappy.Snappy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.LocalTime;

import net.coobird.thumbnailator.Thumbnails;

public class MulticastImageReceiver {
    private static String ipAddress;
    private static JFrame frame;
    private static JLabel imageLabel;
    private static boolean streamIsRunning=false;
    private static boolean isMulticast = false;
    private static final int maxBufferSize = 65507;
    public static void setStreamIsRunning(boolean isRunning){
        streamIsRunning=isRunning;
    }
    public static void initialize(String intIpAddress){
        ipAddress = intIpAddress;
    }
    public static void start() {
        streamIsRunning = true;
        MulticastReceiver multicastReceiver = null;
        UDPServer udpServer = null;
        if(isMulticast) {
            multicastReceiver = (MulticastReceiver) ConnectionFactory.getIConnection(IConnectionNames.MULTICAST_RECEIVER);
            multicastReceiver.initialize(IPorts.STREAM, ipAddress);
        } else {
            udpServer = (UDPServer) ConnectionFactory.getIConnection(IConnectionNames.UDP_SERVER);
            udpServer.initialize(IPorts.UDP_STREAM, null);
        }

        if(frame == null)
            initializeFrame();

        frame.setVisible(true);
        while (streamIsRunning) {
            byte[] fullBuffer;
            if(isMulticast)
                fullBuffer = multicastReceiver.receive();
            else
                fullBuffer = receiveAll(udpServer);

            try (InputStream inStream = new ByteArrayInputStream(fullBuffer)) {

                BufferedImage image = ImageIO.read(inStream);
                if(image == null){
                    continue;
                }
                GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                image = Thumbnails.of(image).forceSize(gd.getDisplayMode().getWidth(), gd.getDisplayMode().getHeight()).asBufferedImage();
                BufferedImage finalImage = image;
                SwingUtilities.invokeLater(() -> {
                    imageLabel.setIcon(new ImageIcon(finalImage));
                    frame.pack();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isMulticast)
            multicastReceiver.close();
        else
            udpServer.close();

        System.out.println("I'm out for some reason!");
    }
    public static void initializeFrame() {
        imageLabel = new JLabel();
        frame = new JFrame("Multicast Image Receiver");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true); // Make the JFrame undecorated
        frame.getContentPane().add(imageLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }
    public static void close() {
        frame.dispose();



    }
    public static void setIsMulticast(boolean multicast){
        isMulticast = multicast;
    }
    private static byte[] receiveAll(UDPServer udpServer) {
        udpServer.setTimeout(3000);
        byte[] fullBuffer = new byte[0];
        while (true) {
            DatagramPacket datagramPacket = udpServer.receivePacket();

            byte[] receivedData = datagramPacket.getData();
            int receivedLength = datagramPacket.getLength();

            // Concatenate received chunk to the full buffer
            byte[] newBuffer = new byte[fullBuffer.length + receivedLength];
            System.arraycopy(fullBuffer, 0, newBuffer, 0, fullBuffer.length);
            System.arraycopy(receivedData, 0, newBuffer, fullBuffer.length, receivedLength);
            fullBuffer = newBuffer;

            if (receivedLength < maxBufferSize) {
                break;
            }
        }
        System.out.println(System.currentTimeMillis());
        return fullBuffer;
    }
}