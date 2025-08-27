package org.aditi.api_automation.config;

import org.aditi.api_automation.handlers.ConfigFactoryProxy;

/**
 * This class is a singleton that holds the configuration factory.
 * The factory is an instance of {@link ConfigFactoryProxy} which is a dynamic proxy that delegates the method calls to
 * the actual {@link ConfigFactory} implementation.
 * The actual implementation is loaded from the configuration file based on the environment.
 * The configuration file is expected to be in the classpath and is named as "config.properties".
 * The environment is determined by the system property "faasos_env" which can be either "dev" or "prod".
 * If the system property is not set, the default environment is "dev".
 * The configuration file is loaded by the {@link CustomConfigManager} which is a singleton that loads the configuration
 * file from the classpath.
 * The properties can be accessed using the {@link ConfigFactory} methods.
 * For example, the base url of the application can be accessed using the {@link ConfigFactory#getBaseUrl()} method.
 *
 */

public class AppConfig {

    private static final ConfigFactory CONFIG_FACTORY = ConfigFactoryProxy.newInstance(CustomConfigManager.getInstance());

    private AppConfig() {
    }

    /**
     * Returns an instance of the configuration factory.
     * @return an instance of the configuration factory
     */
    public static ConfigFactory getConfig() {
        return CONFIG_FACTORY;
    }
}
