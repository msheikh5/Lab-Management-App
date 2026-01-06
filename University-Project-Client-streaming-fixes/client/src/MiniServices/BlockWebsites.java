package MiniServices;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlockWebsites {

    private BlockWebsites(){}
    public static void allowDefault(){
        try {
            // Specify the path to your .bat file
            String batchFilePath = "allowDefault.bat";

            // Create a ProcessBuilder for the .bat file
            CommandRunner.executeCommand(batchFilePath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void allowCustom(String website){
        try {
            AllowWebsite.allowCustomWebsite(website);
            // Specify the path to your .bat file
            String batchFilePath = "allow.bat";

            // Create a ProcessBuilder for the .bat file
            CommandRunner.executeCommand(batchFilePath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void blockCustom(String website){
        try {
            AllowWebsite.blockCustomWebsite(website);
            // Specify the path to your .bat file
            String batchFilePath = "allow.bat";

            // Create a ProcessBuilder for the .bat file
            CommandRunner.executeCommand(batchFilePath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void unblockAll(){
        try {
            // Specify the path to your .bat file
            String batchFilePath = "unblock.bat";

            Files.deleteIfExists(Paths.get("allowedwebsites.txt"));
            Files.deleteIfExists(Paths.get("AllowedWebsites.ser"));
            // Create a ProcessBuilder for the .bat file
            CommandRunner.executeCommand(batchFilePath);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
