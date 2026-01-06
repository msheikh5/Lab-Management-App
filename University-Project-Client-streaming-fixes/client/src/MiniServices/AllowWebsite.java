package MiniServices;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllowWebsite {
    static HashMap<String, Integer> AllowedWebsites = null;
    private static boolean initialized = false;
    private AllowWebsite(){}
    private static void initialize() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("AllowedWebsites.ser"))) {
            Object object = inputStream.readObject();
            if (object instanceof HashMap) {
                //noinspection unchecked
                AllowedWebsites = (HashMap<String, Integer>) object;
            }
        } catch (FileNotFoundException | ClassNotFoundException e) {
            AllowedWebsites = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: Handle this exception
        }
        initialized=true;
    }

    public static void allowCustomWebsite(String website) {
        if (!initialized)
            initialize();
        if (isValid(website)) {
            System.out.println(website + " is already allowed!");
            return;
        }
        BufferedWriter out = getBufferWriter();
        try {
            out.write(website.toLowerCase() + "\n");
            if (website.contains("www.")) {
                URI uri = new URI(website);
                String domain = uri.getHost();
                if (domain != null && domain.startsWith("www.")) {
                    // Remove "www." prefix
                    domain = domain.substring(4);
                }
                out.write(domain + "\n");
            }
            out.flush();
            System.out.println(website + " has been allowed Successfully");
            AllowedWebsites.put(website, 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serialize();
    }

    public static void blockCustomWebsite(String website) {
        if (!initialized)
            initialize();
        try {
            if (!isValid(website)) {
                System.out.println(website + " is already blocked!");
                return;
            }
            // Read the original file
            BufferedReader reader = new BufferedReader(new FileReader("allowedwebsites.txt"));
            List<String> lines = new ArrayList<>();
            String temp = website;
            String line;
            if (website.contains("www.")) {
                URI uri = new URI(website);
                String domain = uri.getHost();
                if (domain != null && domain.startsWith("www.")) {
                    // Remove "www." prefix
                    domain = domain.substring(4);
                }
                website = domain;
            }
            while ((line = reader.readLine()) != null) {

                if (!line.contains(website)) {

                    lines.add(line);
                }
            }
            reader.close();
            website = temp;
            // Write the filtered lines back to the same file
            BufferedWriter writer = new BufferedWriter(new FileWriter("allowedwebsites.txt"));
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                if (i < lines.size() - 1) {
                    writer.newLine(); // Add a newline except for the last line
                }
                writer.flush();
            }
            writer.close();
            System.out.println(website + " has been blocked successfully");
            AllowedWebsites.remove(website);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        serialize();
    }

    private static boolean isValid(String website) {
        return AllowedWebsites.containsKey(website);
    }
    private static void serialize(){
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("AllowedWebsites.ser"))) {
            // Serialize the HashMap
            outputStream.writeObject(AllowedWebsites);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static BufferedWriter getBufferWriter() {
        BufferedWriter out = null;
        FileWriter fstream;
        try {
            fstream = new FileWriter("allowedwebsites.txt", true);
            out = new BufferedWriter(fstream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

}


