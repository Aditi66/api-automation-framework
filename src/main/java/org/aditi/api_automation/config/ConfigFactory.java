/**
 * Interface for managing application configuration.
 * This interface provides methods for retrieving configuration properties
 * and API paths. It is implemented by the ConfigFactoryProxy class.
 *
 * @author Sarbesh Kumar Sarkar
 * @version 1.0
 */
package org.aditi.api_automation.config;

import org.aditi.api_automation.annotations.ConfigProperty;

public interface ConfigFactory {
    /**
     * Retrieves a configuration property by its key.
     *
     * @param key the property key
     * @return the property value
     */
    String getProperty(String key);

    /** Base URLs */
    @ConfigProperty("BASE_URL")
    String getBaseUrl();
    @ConfigProperty("BASE_API_PATH")
    String getBaseApiPath();
    @ConfigProperty("REGISTER_PATH")
    String getRegistrationPath();



    /** API Paths */
    @ConfigProperty(value = "LOGIN_PATH")
    String getLoginPath();
    @ConfigProperty(value = "LOGOUT_PATH")
    String getLogoutPath();
}
