package org.aditi.api_automation.api;

import io.restassured.response.Response;
import org.aditi.api_automation.config.AppConfig;
import org.aditi.api_automation.dto.UserLoginRequest;
import org.aditi.api_automation.requests.AppRequests;

import java.util.function.Function;

import static io.restassured.RestAssured.given;

public class User {

    private User(){}

    private static final Function<UserLoginRequest, Response> userLoginFunction = userLoginRequest -> {
        return given()
                .spec(AppRequests.getRequestFactory()
                        .loginRequest(userLoginRequest.getUsername(),userLoginRequest.getPassword()).build())
                .baseUri(AppConfig.getConfig().getReqResBaseUrl())
                .basePath(AppConfig.getConfig().getReqResBaseApiPath())
                .log().ifValidationFails()
                .when()
                .post(AppConfig.getConfig().getRegistrationPath())
                .then().log()
                .ifError()
                .extract().response();
    };

    public static Response userLogin(UserLoginRequest userLoginRequest){
        return userLoginFunction.apply(userLoginRequest);
    }
}
