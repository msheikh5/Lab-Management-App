import MiniServices.MiniServices;
import Services.CommandReceiver;

import network.*;

import java.io.*;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {


        try {
            Properties appProps = new Properties();
            try (InputStream input = new FileInputStream("Config.properties")) {
                appProps.load(input);
            } catch (FileNotFoundException e) {
                // File not found, create a new one
                System.out.println("Config.properties file not found. Creating a new one...");
                createDefaultConfig(appProps);
            }

            if (args.length != 0) {
                appProps.setProperty("server-ip", args[0]);
                System.out.println("New Server IP: " + appProps.getProperty("server-ip"));

                try (OutputStream output = new FileOutputStream("Config.properties")) {
                    appProps.store(output, "server-ip");
                    System.out.println("Configuration file updated successfully!");
                } catch (IOException e) {
                    throw new RuntimeException("Error updating configuration file.", e);
                }
            } else {
                System.out.println("Last Server IP: " + appProps.getProperty("server-ip"));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration file.", e);
        }
        CommandReceiver commandReceiver=new CommandReceiver();
        commandReceiver.start();

    }
    private static void createDefaultConfig(Properties appProps) {
        appProps.setProperty("server-ip", "default-ip");
        try (OutputStream output = new FileOutputStream("Config.properties")) {
            appProps.store(output, "Default configuration");
            System.out.println("Default configuration file created successfully!");
        } catch (IOException ex) {
            throw new RuntimeException("Error creating default configuration file.", ex);
        }
    }
}