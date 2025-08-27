package org.aditi.api_automation.requests;

import io.restassured.builder.RequestSpecBuilder;
import org.aditi.api_automation.annotations.RequestTemplateFile;

public interface RequestFactory {

    @RequestTemplateFile("requests/login.json")
    RequestSpecBuilder loginRequest(String username, String password);
}
