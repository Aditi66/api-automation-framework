package org.aditi.api_automation.asserts;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public abstract class ValidateResponse<SELF_TYPE extends ValidateResponse<?>> {
    protected SELF_TYPE selfType;
    protected Response response;
    protected SoftAssertions softAssertions;

    protected ValidateResponse(Class<SELF_TYPE> selfType, Response response) {
        this.selfType = selfType.cast(this);
        this.response = response;
        this.softAssertions = new SoftAssertions();
    }

    public SELF_TYPE httpStatusCodeIs(int statusCode) {
        Assertions.assertThat(response.getStatusCode()).describedAs("statusCode")
                .isEqualTo(statusCode).withFailMessage("Status code mismatch");
        return selfType;
    }

    public SELF_TYPE containsValue(String value) {
        softAssertions
                .assertThat(response.getBody().asString())
                .describedAs("responseBody")
                .contains(value);
        return selfType;
    }

    /*
     * Use `https://transform.tools/json-to-json-schema` to generate json schema
     * */
    public SELF_TYPE matchesSchema(String fileClassPath) {
        softAssertions
                .assertThat(response.then().body(matchesJsonSchemaInClasspath(fileClassPath)))
                .describedAs("Schema validation")
                .getWritableAssertionInfo();
        return selfType;
    }

    public SELF_TYPE validateJsonPathData(String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        softAssertions.assertThat(actualValue)
                .isEqualTo(expectedValue);
        return selfType;
    }

    public void assertAll() {
        softAssertions.assertAll();
    }
}
