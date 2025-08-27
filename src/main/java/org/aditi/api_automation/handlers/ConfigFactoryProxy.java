package org.aditi.api_automation.handlers;

import org.aditi.api_automation.annotations.ConfigProperty;
import org.aditi.api_automation.config.ConfigFactory;
import org.aditi.api_automation.config.CustomConfigManager;
import org.aditi.api_automation.exception.PropertyNotFoundException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ConfigFactoryProxy implements InvocationHandler {

    /**
     * The CustomConfigManager instance used to retrieve properties.
     */
    private final CustomConfigManager configManager;

    /**
     * Private constructor to prevent direct instantiation.
     * Use newInstance() to create a new instance.
     *
     * @param configManager the CustomConfigManager instance
     */
    private ConfigFactoryProxy(CustomConfigManager configManager) {
        this.configManager = configManager;
    }

    /**
     * Creates a new instance of ConfigFactoryProxy.
     *
     * @param configManager the CustomConfigManager instance
     * @return a new instance of ConfigFactory
     */
    public static ConfigFactory newInstance(CustomConfigManager configManager) {
        return (ConfigFactory) Proxy.newProxyInstance(
                ConfigFactory.class.getClassLoader(),
                new Class[]{ConfigFactory.class},
                new ConfigFactoryProxy(configManager)
        );
    }

    /**
     * Invokes the specified method on the underlying ConfigFactory instance.
     * Handles property retrieval and exception handling.
     *
     * @param proxy  the proxy instance
     * @param method the method to invoke
     * @param args   the method arguments
     * @return the method result
     * @throws Throwable if an error occurs
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Determine the property name based on the method annotation or name
        String propertyName;
        if (method.isAnnotationPresent(ConfigProperty.class)) {
            // Get the property name from the ConfigProperty annotation
            ConfigProperty annotation = method.getAnnotation(ConfigProperty.class);
            propertyName = annotation.value();
        } else if (method.equals(ConfigFactory.class.getMethod("getProperty", String.class))) {
            // Get the property name from the method argument
            propertyName = (String) args[0];
        } else {
            // Use the method name as the property name (uppercase)
            propertyName = method.getName().toUpperCase();
        }

        // Retrieve the property value from the CustomConfigManager
        String propertyValue = CustomConfigManager.getProperty(propertyName);
        if (propertyValue == null) {
            // Throw an exception if the property is not found
            throw new PropertyNotFoundException("Property not found for key: " + propertyName);
        }

        // Return the property value
        return propertyValue;
    }
}
