package RemoteControl.Server;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
/* Used to recieve server commands then execute them at the client side*/

public class ReceiveEvents extends Thread{
    DataInputStream dataInputStream;
    Robot robot = null;
    private static boolean isRunning=false;
    public static void isRunning(boolean flag){
        isRunning=flag;
    }

    public ReceiveEvents(DataInputStream dataInputStream, Robot robot){

        this.dataInputStream=dataInputStream;
        this.robot = robot;
        start(); //Start the thread and hence calling run method
    }



    public void run(){
        isRunning=true;
        Scanner scanner = null;
        scanner = new Scanner(dataInputStream);
        while(isRunning){
            //recieve commands and respond accordingly

            //while(!scanner.hasNext()) { }
            int command = scanner.nextInt();
            switch(command){
                case-1:

                    System.out.println();
                    robot.mousePress(scanner.nextInt());
                    break;
                case-2:
                    robot.mouseRelease(scanner.nextInt());
                    break;
                case-3:
                    robot.keyPress(scanner.nextInt());
                    break;
                case-4:
                    robot.keyRelease(scanner.nextInt());
                    break;
                case-5:
                    int xScale = scanner.nextInt();
                    int yScale = scanner.nextInt();
                    System.out.println("Mouse move: " + xScale + ", " + yScale);
                    robot.mouseMove(xScale,yScale);
                    break;
            }

        }
    }//end function

}//end class
