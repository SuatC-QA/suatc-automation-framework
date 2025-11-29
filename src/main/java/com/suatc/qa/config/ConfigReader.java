package com.suatc.qa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("config.properties not found in resources");
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        return ConfigReaderHelper.INSTANCE;
    }

    public String getProperty(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String value = properties.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value.trim();
        }

        throw new RuntimeException(
                "Property '" + key + "' not specified in system properties or config.properties."
        );
    }

    public String getProperty(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }

        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue == null ? null : defaultValue.trim();
        }
        return value.trim();
    }

    private static class ConfigReaderHelper {
        private static final ConfigReader INSTANCE = new ConfigReader();
    }

}
