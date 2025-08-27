package org.aditi.api_automation.requests;

import org.aditi.api_automation.handlers.RequestFactoryInvocationHandler;

public class AppRequests {
    private static final RequestFactory REQUEST_FACTORY = RequestFactoryInvocationHandler.createProxy(RequestFactory.class);

    private AppRequests() {}

    public static RequestFactory getRequestFactory() {
        return REQUEST_FACTORY;
    }
}
