package org.aditi.api_automation.handlers;

import org.aditi.api_automation.annotations.RequestTemplateFile;
import lombok.extern.slf4j.Slf4j;
import org.aditi.api_automation.utils.RequestSpecUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestFactoryInvocationHandler implements InvocationHandler {

    public static <T> T createProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class[]{interfaceClass},
                new RequestFactoryInvocationHandler()
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Ensure the method has the correct signature
        Map<String, Object> dynamicValues = new HashMap<>();

        // Get the RequestPayload annotation
        RequestTemplateFile requestTemplateFileAnnotation = method.getAnnotation(RequestTemplateFile.class);
        if (requestTemplateFileAnnotation == null) {
            throw new IllegalStateException("Method not annotated with @RequestPayload");
        }

        String requestFilePath = requestTemplateFileAnnotation.value();

        // Populate dynamic values map from method arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Map) {
                dynamicValues.putAll((Map<String, Object>) args[i]);
            } else {
                dynamicValues.put(method.getParameters()[i].getName(), args[i].toString());
            }
        }
        log.debug("Request Payload: {}", dynamicValues);
        // Create and return the RequestSpecification
        return RequestSpecUtil.createRequestSpecification(requestFilePath, dynamicValues);
    }

}
