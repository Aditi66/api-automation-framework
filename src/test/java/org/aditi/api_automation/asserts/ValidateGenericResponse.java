package org.aditi.api_automation.asserts;

import io.restassured.response.Response;

public class ValidateGenericResponse extends ValidateResponse<ValidateGenericResponse> {

    private ValidateGenericResponse(Response response) {
        super(ValidateGenericResponse.class, response);
    }
    public static ValidateGenericResponse assertThat(Response response) {
        return new ValidateGenericResponse(response);
    }

}
