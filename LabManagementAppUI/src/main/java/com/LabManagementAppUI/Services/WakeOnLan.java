package com.LabManagementAppUI.Services;

import com.LabManagementAppUI.Manager.ConfigurationManager;
import com.LabManagementAppUI.Manager.NetworkManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WakeOnLan {

    public static final int PORT = 7;
    public static void wakeOnLan(List<String> IPs) {
        ConfigurationManager.getInstance().loadConfigurationFromFile();
        Map<String, String> MACAddresses = ConfigurationManager.getInstance().getMACAddresses();
        for (String ipAddress : IPs) {
            try {
                byte[] macBytes = getMacBytes(MACAddresses.get(ipAddress));
                byte[] bytes = new byte[6 + 16 * macBytes.length];
                for (int i = 0; i < 6; i++) {
                    bytes[i] = (byte) 0xff;
                }
                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }
                InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);

                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();

                System.out.println("Wake-on-LAN packet sent.");
            } catch (Exception e) {
                System.out.println("Failed to send Wake-on-LAN packet: " + e.getMessage());
            }
        }
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");

        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address.");
        }

        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }

    public static String getMacAddress(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("arp -a " + ipAddress);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(ipAddress)) {
                    Pattern pattern = Pattern.compile("([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})");
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        return matcher.group();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
