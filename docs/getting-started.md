# Getting Started

This guide will walk you through setting up and running your first API test with the framework.

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
  ```bash
  java -version
  ```
- **Maven 3.6** or higher
  ```bash
  mvn -version
  ```
- **Git**
  ```bash
  git --version
  ```
- **IDE** (IntelliJ IDEA or Eclipse recommended)

## Step 1: Project Setup

### Clone the Repository
```bash
git clone https://github.com/Aditi66/api-automation-framework.git
cd api-automation-framework
```

### Build the Project
```bash
mvn clean install
```

This will:
- Download all dependencies
- Compile the source code
- Run existing tests
- Create the project artifacts

## Step 2: Understanding the Project Structure

Take a moment to explore the project structure:

```
api-automation-framework/
├── src/
│   ├── main/java/org/aditi/api_automation/
│   │   ├── api/User.java                    # Example API class
│   │   ├── config/                          # Configuration management
│   │   ├── requests/                        # Request factories
│   │   └── ...
│   ├── main/resources/
│   │   ├── environment/                     # Environment configs
│   │   ├── requests/                        # JSON templates
│   │   └── response/schemas/                # JSON schemas
│   └── test/
│       ├── java/org/aditi/api_automation/
│       │   ├── steps/UserSteps.java         # Example step definitions
│       │   └── ...
│       └── resources/features/
│           └── reqres/User.feature          # Example feature file
```

## Step 3: Run Your First Test

### Execute All Tests
```bash
mvn test
```

### Run Specific Test
```bash
mvn test -Dtest=UserSteps
```

### Run with TestNG
```bash
mvn test -DsuiteXmlFile=testng.xml
```

## Step 4: Create Your First API Test

Let's create a simple test to understand the framework workflow.

### Step 4.1: Create a New API Class

Create `src/main/java/org/aditi/api_automation/api/PostApi.java`:

```java
package org.aditi.api_automation.api;

import io.restassured.response.Response;
import org.aditi.api_automation.config.AppConfig;

import static io.restassured.RestAssured.given;

public class PostApi {
    
    private PostApi() {}
    
    public static Response getPostById(String postId) {
        return given()
                .baseUri(AppConfig.getConfig().getReqResBaseUrl())
                .basePath(AppConfig.getConfig().getReqResBaseApiPath())
                .log().ifValidationFails()
                .when()
                .get("/posts/" + postId)
                .then()
                .log().ifError()
                .extract().response();
    }
    
    public static Response createPost(String title, String body, String userId) {
        return given()
                .baseUri(AppConfig.getConfig().getReqResBaseUrl())
                .basePath(AppConfig.getConfig().getReqResBaseApiPath())
                .contentType("application/json")
                .body("{\"title\":\"" + title + "\",\"body\":\"" + body + "\",\"userId\":" + userId + "}")
                .log().ifValidationFails()
                .when()
                .post("/posts")
                .then()
                .log().ifError()
                .extract().response();
    }
}
```

### Step 4.2: Create a Feature File

Create `src/test/resources/features/reqres/Post.feature`:

```gherkin
Feature: Post Management

  Scenario: Get post by ID
    Given I want to retrieve a post
    When I request post with ID "1"
    Then I should receive a successful response
    And the response should contain post details

  Scenario: Create a new post
    Given I want to create a new post
    When I create a post with title "Test Post" and body "Test Body" for user "1"
    Then I should receive a successful response
    And the response should contain the created post details
```

### Step 4.3: Create Step Definitions

Create `src/test/java/org/aditi/api_automation/steps/PostSteps.java`:

```java
package org.aditi.api_automation.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.aditi.api_automation.api.PostApi;
import org.aditi.api_automation.asserts.ValidateGenericResponse;

@Slf4j
public class PostSteps {
    
    private Response response;
    private String postId;
    private String title;
    private String body;
    private String userId;
    
    @Given("I want to retrieve a post")
    public void i_want_to_retrieve_a_post() {
        // Setup code if needed
    }
    
    @When("I request post with ID {string}")
    public void i_request_post_with_id(String id) {
        this.postId = id;
        this.response = PostApi.getPostById(id);
        log.info("Response: {}", response.getBody().asString());
    }
    
    @Given("I want to create a new post")
    public void i_want_to_create_a_new_post() {
        // Setup code if needed
    }
    
    @When("I create a post with title {string} and body {string} for user {string}")
    public void i_create_a_post_with_title_and_body_for_user(String title, String body, String userId) {
        this.title = title;
        this.body = body;
        this.userId = userId;
        this.response = PostApi.createPost(title, body, userId);
        log.info("Response: {}", response.getBody().asString());
    }
    
    @Then("I should receive a successful response")
    public void i_should_receive_a_successful_response() {
        ValidateGenericResponse.assertThat(response)
            .httpStatusCodeIs(200);
    }
    
    @Then("the response should contain post details")
    public void the_response_should_contain_post_details() {
        ValidateGenericResponse.assertThat(response)
            .validateJsonPathData("id", Integer.parseInt(postId))
            .containsValue("title")
            .containsValue("body");
    }
    
    @Then("the response should contain the created post details")
    public void the_response_should_contain_the_created_post_details() {
        ValidateGenericResponse.assertThat(response)
            .validateJsonPathData("title", title)
            .validateJsonPathData("body", body)
            .validateJsonPathData("userId", Integer.parseInt(userId))
            .containsValue("id");
    }
}
```

### Step 4.4: Run Your New Test

```bash
mvn test -Dtest=PostSteps
```

## Step 5: Using Request Templates

Let's create a more sophisticated test using request templates.

### Step 5.1: Create Request Template

Create `src/main/resources/requests/post-create.json`:

```json
{
  "headers": {
    "Content-Type": "application/json"
  },
  "body": {
    "title": "{{title}}",
    "body": "{{body}}",
    "userId": "{{userId}}"
  }
}
```

### Step 5.2: Update RequestFactory

Add to `src/main/java/org/aditi/api_automation/requests/RequestFactory.java`:

```java
@RequestTemplateFile("requests/post-create.json")
RequestSpecBuilder createPostRequest(String title, String body, String userId);
```

### Step 5.3: Update API Class

Update `PostApi.java` to use the template:

```java
public static Response createPostWithTemplate(String title, String body, String userId) {
    return given()
            .spec(AppRequests.getRequestFactory()
                .createPostRequest(title, body, userId).build())
            .baseUri(AppConfig.getConfig().getReqResBaseUrl())
            .basePath(AppConfig.getConfig().getReqResBaseApiPath())
            .log().ifValidationFails()
            .when()
            .post("/posts")
            .then()
            .log().ifError()
            .extract().response();
}
```

## Step 6: Schema Validation

### Step 6.1: Create JSON Schema

Create `src/main/resources/response/schemas/post-schema.json`:

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "id": {
      "type": "number"
    },
    "title": {
      "type": "string"
    },
    "body": {
      "type": "string"
    },
    "userId": {
      "type": "number"
    }
  },
  "required": ["id", "title", "body", "userId"]
}
```

### Step 6.2: Add Schema Validation to Test

Update your step definition:

```java
@Then("the response should match the post schema")
public void the_response_should_match_the_post_schema() {
    ValidateGenericResponse.assertThat(response)
        .matchesSchema("response/schemas/post-schema.json");
}
```

## Step 7: Configuration Management

### Step 7.1: Add New Configuration Properties

Add to `src/main/java/org/aditi/api_automation/config/ConfigFactory.java`:

```java
@ConfigProperty("POSTS_PATH")
String getPostsPath();
```

Add to `src/main/resources/api.properties`:

```properties
POSTS_PATH=/posts
```

### Step 7.2: Use Configuration in API

Update your API class:

```java
public static Response getPostById(String postId) {
    return given()
            .baseUri(AppConfig.getConfig().getReqResBaseUrl())
            .basePath(AppConfig.getConfig().getReqResBaseApiPath())
            .log().ifValidationFails()
            .when()
            .get(AppConfig.getConfig().getPostsPath() + "/" + postId)
            .then()
            .log().ifError()
            .extract().response();
}
```

## Next Steps

Now that you've completed the getting started guide:

1. **Explore the existing examples** in the test directory
2. **Read the API reference** for detailed method documentation
3. **Check the best practices** for coding standards
4. **Join the community** by contributing to the project

## Common Issues and Solutions

### Issue: Tests not found
**Solution**: Ensure feature files are in `src/test/resources/features/` and step definitions are in `src/test/java/org/aditi/api_automation/steps/`

### Issue: Configuration properties not found
**Solution**: Check that properties are defined in the correct environment file and the `@ConfigProperty` annotation is used correctly

### Issue: Request templates not loading
**Solution**: Verify the JSON template file exists and the `@RequestTemplateFile` annotation path is correct

### Issue: Schema validation failing
**Solution**: Use tools like [JSON Schema Generator](https://transform.tools/json-to-json-schema) to create accurate schemas

---

**Congratulations! You've successfully set up and run your first API test with the framework! 🎉**
