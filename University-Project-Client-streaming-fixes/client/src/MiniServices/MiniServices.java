package MiniServices;

public class MiniServices {
    private MiniServices(){}
    public static void openApp(String command) throws Exception {
        CommandRunner.executeCommand(command);
        //String s="start "+browser+" \""+URL+"\"";
    }
    public static void shutDown() throws Exception {
        CommandRunner.executeCommand("shutdown -s -t 0");
    }
    public static void disableMouse() throws Exception{
        CommandRunner.executeCommand("for /f \"tokens=3\" %i in ('pnputil /enum-devices /class mouse ^| findstr /C:\"Instance ID\"') " +
                "do (pnputil /disable-device %i)");
    }
    public static void enableMouse() throws Exception{
        CommandRunner.executeCommand("for /f \"tokens=3\" %i in ('pnputil /enum-devices /class mouse ^| findstr /C:\"Instance ID\"') " +
                "do (pnputil /enable-device %i)");
    }
    public static void disableKeyboard() throws Exception {
        CommandRunner.executeCommand("for /f \"tokens=3\" %i in ('pnputil /enum-devices /class keyboard ^| findstr /C:\"Instance ID\"') do (pnputil /remove-device %i)");
    }
    public static void enableKeyboard() throws Exception {
        CommandRunner.executeCommand("pnputil /scan-devices");
    }
    public static void blockUsb() throws Exception {
        CommandRunner.executeCommand("reg add HKLM\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v Start /t REG_DWORD /d 4 /f");
    }
    public static void unblockUsb() throws Exception {
        CommandRunner.executeCommand("reg add HKLM\\SYSTEM\\CurrentControlSet\\Services\\USBSTOR /v Start /t REG_DWORD /d 3 /f");
    }
}
