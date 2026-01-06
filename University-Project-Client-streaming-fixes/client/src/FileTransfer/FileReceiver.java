package FileTransfer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import auxiliaryClasses.IPorts;
import network.ConnectionFactory;
import network.IConnectionNames;
import network.TCPClient;
public class FileReceiver implements Runnable{
    private final String serverIp;
    private TCPClient connection;
    private String clientSavePath;
    public FileReceiver(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        connection= (TCPClient) ConnectionFactory.getIConnection(IConnectionNames.TCP_CLIENT);
        connection.initialize(IPorts.FILE_TRANSFER, serverIp);

        clientSavePath = connection.receiveString();
        boolean isDirectory = connection.receiveBoolean();
        if (isDirectory) {
            receiveDirectory();
        } else {
            receiveFile();
        }
        connection.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime-startTime) + " milliseconds");
    }

    private void receiveFile() {
        String fileName = connection.receiveString();

        // Receive the file content
        int fileSize = connection.receiveInt();
        long iterations = fileSize / 1024;
        byte[] fileContent = new byte[fileSize];

        connection.receiveFile(fileContent);

        // Save the file to the local filesystem
        Path filePath = Path.of(clientSavePath + "\\" + fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, fileContent);
        } catch (IOException e) {
            System.out.println("Couldn't write file " + fileName + " to path " + filePath);
        }

        System.out.println("File received: " + filePath.toString());
    }

    private void receiveDirectory() {
        String directoryName = connection.receiveString();
        int numberOfFiles = connection.receiveInt();
        clientSavePath = clientSavePath + "\\" + directoryName;
        for(int i=0;i<numberOfFiles;i++)
            receiveFile();
    }

}
