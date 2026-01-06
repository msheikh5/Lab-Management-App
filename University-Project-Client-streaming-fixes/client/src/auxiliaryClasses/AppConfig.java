package auxiliaryClasses;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static final String DEFAULT_SERVER_IP = "192.168.2.12";
    private static final String SERVER_IP_KEY = "server-ip";

    private Map<String, String> properties;

    public AppConfig() {
        this.properties = new HashMap<>();
        loadDefaultProperties();
    }

    private void loadDefaultProperties() {
        properties.put(SERVER_IP_KEY, DEFAULT_SERVER_IP);
    }

    public String getServerIP() {
        return properties.get(SERVER_IP_KEY);
    }

    public void setServerIP(String newServerIP) {
        properties.put(SERVER_IP_KEY, newServerIP);
    }
}
