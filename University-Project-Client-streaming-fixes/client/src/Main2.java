import MiniServices.Screen;
import Services.Handler;
import network.*;

import java.util.Arrays;

public class Main2 {

    public static void main(String[] args) throws Exception {
//        UDPClient udpClient=new UDPClient();
//        udpClient.initialize(4000,"localhost");
//        udpClient.send("message sent");
//        System.out.println("sent message");
//        System.out.println(udpClient.receive());
//        udpClient.close();
//
//        System.out.println("done");

//        MulticastReceiver multicastReceiver=new MulticastReceiver();
//        multicastReceiver.initialize(4000,"239.0.0.1");
//        multicastReceiver.receive();
//        multicastReceiver.close();
        Screen.turnOnScreen();
        Thread.sleep(10000);
        Screen.turnOffScreen();

    }
}
