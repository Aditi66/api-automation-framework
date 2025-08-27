package org.aditi.api_automation.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import lombok.extern.slf4j.Slf4j;
import org.aditi.api_automation.config.AppConfig;
import org.aditi.api_automation.constants.FrameworkConstants;

@Slf4j
public class SpecFactory {
    private SpecFactory() {}

    private static RequestSpecBuilder getBaseSpecBuilder() {
        log.debug("Creating base request specification");

        return new RequestSpecBuilder()
                .setBaseUri(AppConfig.getConfig().getBaseUrl())
                .setBasePath(AppConfig.getConfig().getBaseApiPath())
                .setConfig(RestAssuredConfig.config()
                        .logConfig(LogConfig.logConfig().blacklistHeader(FrameworkConstants.AUTHORIZATION_HEADER)));
    }

    public static RequestSpecBuilder getSpecBuilder() {
        log.debug("Creating request specification");
        return getBaseSpecBuilder();
    }
}
