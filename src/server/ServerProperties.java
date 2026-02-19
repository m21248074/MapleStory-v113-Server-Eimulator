package server;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration.JupIOFactory;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.io.FileHandler;

import database.DatabaseConnection;

/**
 *
 * @author Emilyx3
 */
public class ServerProperties {

    private static final Properties props = new Properties();

    private static final String[] toLoad = {
        "Settings.ini"
    };

    private ServerProperties() {
    }

    static {
        loadProperties();
    }

    public static void loadProperties() {
        for (String s : toLoad) {
            InputStreamReader fr;
            try {
                fr = new InputStreamReader(new FileInputStream(s), "UTF-8");
                props.load(fr);
                fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void applyProperties(Map<String, String> uiData) {
        uiData.forEach((key, value) -> setProperty(key, value));
    }

    public static void saveProperties(Map<String, String> uiData) {
        try {
            Parameters params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(params.properties()
                        .setFileName("Settings.ini")
                        .setEncoding("UTF-8"));

            PropertiesConfiguration config = builder.getConfiguration();

            config.setIOFactory(new JupIOFactory(false));

            uiData.forEach((key, value) -> {
                config.setProperty(key, value);
            });

            builder.save();
            
            applyProperties(uiData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String s) {
        return props.getProperty(s);
    }

    public static void setProperty(String prop, String newInf) {
        props.setProperty(prop, newInf);
    }

    public static String getProperty(String s, String def) {
        return props.getProperty(s, def);
    }

    public static int getIntProperty(String s, String def) {
        return Integer.valueOf(props.getProperty(s, def));
    }
}
