package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println(Commands.OPEN_
                WEBSITE);
        System.out.println(Commands.OPEN_WEBSITE.label);

    }
    private static String computerIDFromIP(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        return octets[octets.length-1];
    }
}