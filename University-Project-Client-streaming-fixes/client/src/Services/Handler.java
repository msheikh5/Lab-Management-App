package Services;

import FileTransfer.FileReceiver;
import FileTransfer.FileSender;
import MiniServices.MiniServices;
import RemoteControl.Server.Capture;
import RemoteControl.Server.ReceiveEvents;
import RemoteControl.Server.Sender;
import Stream.MulticastImageReceiver;
import auxiliaryClasses.IPorts;
import network.ConnectionFactory;
import network.IConnectionNames;
import network.TCPServer;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.util.*;

import MiniServices.Screen;
import MiniServices.BlockWebsites;
public class Handler implements Runnable {
    public static volatile Queue<byte[]> baos =new LinkedList<>();
    private static TCPServer tcpServer;
    private static String command;

    public static void setCommand(String command) {
        Handler.command = command;
    }

    public Handler (){}
    public static void startStream(boolean multicast) {
        MulticastImageReceiver.setIsMulticast(multicast);
        MulticastImageReceiver.setStreamIsRunning(true);
        MulticastImageReceiver.initialize("192.168.2.12");
        MulticastImageReceiver.start();

//        MiniServices.enableKeyboard();
    }

    public static void closeStream() {
        MulticastImageReceiver.setStreamIsRunning(false);
        MulticastImageReceiver.close();
//        MiniServices.enableKeyboard();
    }
    public static void startControl() throws Exception {
        Thread t1=new Thread(new Capture());
        t1.start();
        Thread t2=new Thread(new Sender());
        t2.start();
        ServerSocket socket = null;
        System.out.println("sending images");
        Socket sc = null;
        Robot robot = null;
        try {
            tcpServer = (TCPServer) ConnectionFactory.getIConnection(IConnectionNames.TCP_SERVER);
            tcpServer.initialize(IPorts.CONTROL+10,null);
            //socket = new ServerSocket(1235);
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
            robot = new Robot(gDev);
            //sc = socket.accept();
            Thread t3=new Thread(new ReceiveEvents(tcpServer.getInputStream(), robot));
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
    public static void stopControl() throws Exception {
        MiniServices.enableKeyboard();
        Capture.isRunning(false);
        Sender.isRunning(false);
        ReceiveEvents.isRunning(false);
        tcpServer.close();
    }
    public static void receiveFile() {
       /* Properties appProps = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        try {
            appProps.load(new FileInputStream(rootPath + "Config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Config.properties file is missing!");
        }*/
        Thread fileReceiver = new Thread(new FileReceiver("192.168.2.12"));
        fileReceiver.start();
    }
    public static void sendFile() {
        /*Properties appProps = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        try {
            appProps.load(new FileInputStream(rootPath + "Config.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Config.properties file is missing!");
        }*/
        Thread fileSender = new Thread(new FileSender("192.168.2.12"));
        fileSender.start();
    }



    @Override
    public void run() {
        String commandName = command;
        String commandParam = null;

        if(commandName.contains("Website")){
            commandName = command.substring(0, command.indexOf(':'));
            commandParam = command.substring(command.indexOf(':') + 1);
        }
        switch (commandName){
            case "Stream":
                Handler.startStream(true);
                break;
            case "Close Stream":
                Handler.closeStream();
                Screen.turnOffScreen();
                break;
            case "UDP Stream":
                try {
                    MiniServices.disableKeyboard();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Handler.startStream(false);
            case "Close UDP Stream":
                try {
                    MiniServices.enableKeyboard();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                finally {
                    Handler.closeStream();
                }
                break;
            case "Control":
                try {
                    Handler.startControl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Stop Control":
                try {
                    Handler.stopControl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "File Transfer":
                Handler.receiveFile();
                break;
            case "File Collect":
                Handler.sendFile();
                break;
            case "Shutdown":
                try {
                    MiniServices.shutDown();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Freeze":
                Screen.turnOnScreen();
                try {
                    MiniServices.disableKeyboard();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    MiniServices.disableMouse();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Unfreeze":
                Screen.turnOffScreen();
                try {
                    MiniServices.enableKeyboard();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                try {
                    MiniServices.enableMouse();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Allow Default":
                BlockWebsites.allowDefault();
                break;
            case "Unblock All":
                BlockWebsites.unblockAll();
                break;
            case "Allow Website":
                BlockWebsites.allowCustom(commandParam);
                break;
            case "Block Website":
                BlockWebsites.blockCustom(commandParam);
                break;
            case "Open Website":
                try {
                    MiniServices.openApp(commandParam);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Block USB":
                try {
                    MiniServices.blockUsb();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            case "Unblock USB":
                try {
                    MiniServices.unblockUsb();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                System.out.println("Command not found");

        }
    }
}
