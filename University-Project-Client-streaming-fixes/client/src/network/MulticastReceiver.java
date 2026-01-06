package network;

import java.io.IOException;
import java.net.*;

public class MulticastReceiver implements IConnection {

    private final int MAX_BUFFER_SIZE = 65507;
    private int port;
    private InetAddress group = null;
    private MulticastSocket multicastSocket = null;
    private NetworkInterface networkInterface = null;

    public void initialize(int port, String ipAddress) {
        try {
            InetAddress multicastGroup = InetAddress.getByName(ipAddress);
            multicastSocket = new MulticastSocket(port);// Multicast IP address
            multicastSocket.joinGroup(multicastGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*InetAddress multicastGroup = null; // Multicast IP address
        try {
            multicastGroup = InetAddress.getByName(ipAddress);
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(multicastGroup);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    @Override
    public void send(byte[] data) {

    }

    @Override
    public void sendString(String message) {

    }

    @Override
    public byte[] receive() {
        try {
            multicastSocket.setSoTimeout(3000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] receiveData = new byte[MAX_BUFFER_SIZE];
        byte[] fullBuffer = new byte[0];
        while (true) {
            DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                multicastSocket.receive(datagramPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] receivedData = datagramPacket.getData();
            int receivedLength = datagramPacket.getLength();

            // Concatenate received chunk to the full buffer
            byte[] newBuffer = new byte[fullBuffer.length + receivedLength];
            System.arraycopy(fullBuffer, 0, newBuffer, 0, fullBuffer.length);
            System.arraycopy(receivedData, 0, newBuffer, fullBuffer.length, receivedLength);
            fullBuffer = newBuffer;

            if (receivedLength < MAX_BUFFER_SIZE) {
                break;
            }
        }

        return fullBuffer;

    }

    @Override
    public String receiveString() {
        return null;
    }


    public void close() {
        try {
            multicastSocket.leaveGroup(new InetSocketAddress(group, port), networkInterface);
            multicastSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
