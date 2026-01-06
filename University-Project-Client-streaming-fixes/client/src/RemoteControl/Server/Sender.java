package RemoteControl.Server;

import Services.Handler;
import auxiliaryClasses.IPorts;
import network.ConnectionFactory;
import network.IConnectionNames;
import network.UDPClient;

public class Sender implements Runnable {
    private final int chunkSize= 65507;
    private static boolean isRunning=false;
    public static void isRunning(boolean flag){
        isRunning=flag;
    }
    @Override
    public void run() {

        try{
            UDPClient udpClient= (UDPClient) ConnectionFactory.getIConnection(IConnectionNames.UDP_CLIENT);
            udpClient.initialize(IPorts.CONTROL,"192.168.2.12");//server ip from config file

            isRunning=true;
            while(isRunning){
                //System.out.println("Hello from sender");
                if(Handler.baos.isEmpty())
                    continue;
                sendImage(udpClient);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    private void sendImage(UDPClient udpClient){

        byte[] compressed= Handler.baos.remove();
        int totalChunks = (int) Math.ceil((double) compressed.length / chunkSize);

        for (int chunkIndex = 0; chunkIndex < totalChunks; chunkIndex++) {
            int offset = chunkIndex * chunkSize;
            int length = Math.min(chunkSize, compressed.length - offset);
            byte[] chunkBuffer = new byte[length];
            System.arraycopy(compressed, offset, chunkBuffer, 0, length);

            udpClient.send(chunkBuffer);
        }
    }
}
