package org.aditi.api_automation.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.aditi.api_automation.api.User;
import org.aditi.api_automation.asserts.ValidateGenericResponse;
import org.aditi.api_automation.dto.UserLoginRequest;

@Slf4j
public final class UserSteps {

    private Response response;
    private UserLoginRequest userLoginRequest;

    @Given("I want to register a new user")
    public void i_want_to_register_a_new_user() {
        this.userLoginRequest = new UserLoginRequest("aditi", "Pass@123");
    }

    @When("I send a POST request to register api")
    public void i_send_a_post_request_to_register_api() {
        this.response = User.userLogin(this.userLoginRequest);
        log.info("Response: {}", response);
    }

    @Then("I should receive a successful response")
    public void i_should_receive_a_successful_response() {
        ValidateGenericResponse.assertThat(this.response)
                .httpStatusCodeIs(200)
                .matchesSchema("response/schemas/registration_schema.json");
    }


}
