package org.aditi.api_automation.config;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * CustomConfigManager is a singleton class responsible for loading and managing
 * application configuration properties.
 *
 * <p>This class loads the configuration properties from the environment,
 * system properties, and configuration files.
 *
 * @author Aditi
 *  @version 1.0.0
 *  @since 1.0.0
 */
@Slf4j
public class CustomConfigManager {

    // Singleton instance of the CustomConfigManager
    private static CustomConfigManager instance;

    // Properties object to store the configuration properties
    private static final Properties properties = new Properties();

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Loads the properties in the constructor.
     */
    private CustomConfigManager() {
        loadProperties();
    }

    /**
     * Public method to get the singleton instance of CustomConfigManager.
     *
     * @return the singleton instance of CustomConfigManager
     */
    public static synchronized CustomConfigManager getInstance() {
        if (instance == null) {
            instance = new CustomConfigManager();
        }
        return instance;
    }

    /**
     * Method to load the configuration properties from the environment,
     * system properties, and configuration files.
     */
    private void loadProperties() {
        // Load system properties
        properties.putAll(System.getProperties());

        // Load environment variables
        properties.putAll(System.getenv());

        // Get the environment type (default to "local" if not set)
        String env = properties.getProperty("environment", "local");
        log.info("Fetching config for Environment: {}", env);

        // Construct the file paths for environment-specific and default config files
        String envFilePath = String.format("src/main/resources/environment/%s.properties", env);
        String apiFilePath = "src/main/resources/api.properties";

        // Create Properties objects to load the config files
        Properties configProperties = new Properties();
        Properties envProperties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(envFilePath)) {
            // Load environment-specific config file
            envProperties.load(fileInputStream);
            properties.putAll(envProperties);
        } catch (Exception e) {
            // Throw a RuntimeException if there's an error loading the config file
            throw new RuntimeException(e);
        }

        try (FileInputStream fileInputStream = new FileInputStream(apiFilePath)) {
            // Load default config file
            configProperties.load(fileInputStream);
            properties.putAll(configProperties);
        } catch (Exception e) {
            // Throw a RuntimeException if there's an error loading the config file
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to get a configuration property by its key.
     *
     * @param key the key of the property to get
     * @return the value of the property, or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Method to validate the configuration properties.
     * Checks if the properties are empty and if the base URL is set.
     */
    public static void validateConfig() {
        log.debug("Validating config");
        if(properties.isEmpty()) {
            // Throw a RuntimeException if the properties are empty
            throw new RuntimeException("Configuration file is empty or missing required properties.");
        }

        // Get the base URL
        String baseUrl = getProperty("base_url");
        if (baseUrl == null) {
            // Throw a RuntimeException if the base URL is not set
            throw new RuntimeException("Base URL not found in config file");
        }
        log.debug("Base URL: {}", baseUrl);
    }

}
