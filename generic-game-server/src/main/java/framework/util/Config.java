package framework.util;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private Config() { /* utility class */ }

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(Config.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not load config.properties", e);
        }
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

    public static int getInteger(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }


}
