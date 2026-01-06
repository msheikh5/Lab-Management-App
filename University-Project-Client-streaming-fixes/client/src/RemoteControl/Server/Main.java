package RemoteControl.Server;

import network.ConnectionFactory;
import network.IConnectionNames;
import network.TCPServer;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static volatile Queue<byte[]> baos =new LinkedList<>();
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(new Capture());
        t1.start();
        Thread t2=new Thread(new Sender());
        t2.start();
        ServerSocket socket = null;

        Socket sc = null;
        Robot robot = null;
        try {
            TCPServer tcpServer = (TCPServer) ConnectionFactory.getIConnection(IConnectionNames.TCP_SERVER);
            tcpServer.initialize(50022,null);
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
}