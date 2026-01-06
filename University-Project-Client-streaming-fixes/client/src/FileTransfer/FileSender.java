package FileTransfer;

import auxiliaryClasses.IPorts;
import network.ConnectionFactory;
import network.IConnectionNames;
import network.TCPClient;

import java.io.*;


public class FileSender implements Runnable{
    private final String serverIp;
    private String senderFilePath;
    private TCPClient connection;
    public FileSender(String serverIp) {
        this.serverIp = serverIp;
    }

    @Override
    public void run() {
        connection= (TCPClient) ConnectionFactory.getIConnection(IConnectionNames.TCP_CLIENT);
        connection.initialize(IPorts.FILE_TRANSFER,serverIp);

        senderFilePath = connection.receiveString();

        File file = new File(senderFilePath);
        if (file.isDirectory()) {
            connection.sendBoolean(true);
            sendDirectory(file);
        } else {
            connection.sendBoolean(false);
            sendFile(file);
        }
        connection.close();
    }

    private void sendFile(File file) {
        connection.sendString(file.getName());
        int fileSize = (int) file.length();
        connection.sendInt(fileSize);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                connection.sendFileData(buffer, bytesRead);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File " + file.getName() + " is not found.");
        } catch (IOException e) {
            System.out.println("Couldn't read file " + file.getName());
        }

    }

    private void sendDirectory(File directory) {
        connection.sendString(directory.getName());
        File[] files = directory.listFiles();
        // Send the number of files in the directory
        connection.sendInt(files != null ? files.length : 0);
        if (files != null) {
            // Send each file in the directory
            for (File file : files) {
                if (file.isDirectory()) {
                    sendDirectory(file);
                } else {
                    sendFile(file);
                }
            }
        }

    }
}
