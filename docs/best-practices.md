# Best Practices

This guide provides comprehensive best practices for using the API Automation Framework effectively and maintaining high-quality test code.

## Code Organization

### 1. Package Structure

Follow the established package structure for consistency:

```
org.aditi.api_automation/
├── api/                    # API domain classes
├── config/                 # Configuration management
├── dto/                    # Data transfer objects
├── requests/               # Request factories
├── asserts/                # Validation classes
├── steps/                  # Cucumber step definitions
└── utils/                  # Utility classes
```

### 2. File Organization

- **One API class per domain**: Create separate classes for different API domains (User, Product, Order, etc.)
- **Group related tests**: Keep related scenarios in the same feature file
- **Separate concerns**: Keep API logic, test logic, and validation logic separate

### 3. Class Design Principles

```java
// Good: Single responsibility, static methods
public class UserApi {
    private UserApi() {} // Prevent instantiation
    
    public static Response getUserById(String userId) {
        // Implementation
    }
    
    public static Response createUser(UserCreateRequest request) {
        // Implementation
    }
}

// Avoid: Mixed responsibilities
public class UserApi {
    public void doEverything() {
        // Don't mix API calls, validation, and business logic
    }
}
```

## Naming Conventions

### 1. Class Names

- **API Classes**: `{Domain}Api` (e.g., `UserApi`, `ProductApi`, `OrderApi`)
- **Step Classes**: `{Domain}Steps` (e.g., `UserSteps`, `ProductSteps`)
- **DTO Classes**: `{Action}{Domain}Request/Response` (e.g., `UserLoginRequest`, `ProductCreateResponse`)
- **Validation Classes**: `Validate{Type}Response` (e.g., `ValidateGenericResponse`)

### 2. Method Names

- **API Methods**: Use descriptive action names (e.g., `getUserById`, `createUser`, `updateUser`)
- **Step Methods**: Use Gherkin-style names (e.g., `i_want_to_get_user_information`)
- **Validation Methods**: Use descriptive validation names (e.g., `httpStatusCodeIs`, `containsValue`)

### 3. Variable Names

```java
// Good: Descriptive names
private Response userResponse;
private String userId;
private UserLoginRequest loginRequest;

// Avoid: Generic names
private Response response;
private String id;
private Object request;
```

### 4. Feature File Names

- Use descriptive, domain-focused names
- Use kebab-case for file names
- Group related features in subdirectories

```
features/
├── user/
│   ├── user-authentication.feature
│   ├── user-profile.feature
│   └── user-permissions.feature
├── product/
│   ├── product-catalog.feature
│   └── product-inventory.feature
└── order/
    ├── order-creation.feature
    └── order-status.feature
```

## Configuration Management

### 1. Environment-Specific Configuration

```properties
# environment/dev.properties
BASE_URL=https://dev-api.example.com
API_VERSION=v1
TIMEOUT=30000

# environment/prod.properties
BASE_URL=https://api.example.com
API_VERSION=v2
TIMEOUT=60000
```

### 2. Property Naming

- Use UPPER_CASE for all property names
- Use descriptive, hierarchical names
- Group related properties with prefixes

```properties
# Good
USER_API_BASE_URL=https://user-api.example.com
USER_API_TIMEOUT=30000
PRODUCT_API_BASE_URL=https://product-api.example.com
PRODUCT_API_TIMEOUT=30000

# Avoid
URL1=https://api1.example.com
URL2=https://api2.example.com
```

### 3. Configuration Validation

```java
// Validate required properties at startup
@BeforeClass
public static void validateConfiguration() {
    assertNotNull("BASE_URL is required", AppConfig.getConfig().getBaseUrl());
    assertNotNull("API_VERSION is required", AppConfig.getConfig().getApiVersion());
}
```

## API Test Design

### 1. Test Structure

Follow the Arrange-Act-Assert pattern:

```java
@Given("I have valid user credentials")
public void i_have_valid_user_credentials() {
    // Arrange: Setup test data
    loginRequest = new UserLoginRequest("testuser", "testpass");
}

@When("I attempt to login")
public void i_attempt_to_login() {
    // Act: Perform the action
    response = UserApi.login(loginRequest);
}

@Then("I should receive a successful response")
public void i_should_receive_a_successful_response() {
    // Assert: Validate the result
    ValidateGenericResponse.assertThat(response)
        .httpStatusCodeIs(200);
}
```

### 2. Test Data Management

```java
// Use data builders for complex objects
public class UserLoginRequestBuilder {
    private String username = "defaultuser";
    private String password = "defaultpass";
    
    public UserLoginRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public UserLoginRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }
    
    public UserLoginRequest build() {
        return new UserLoginRequest(username, password);
    }
}

// Usage
UserLoginRequest request = new UserLoginRequestBuilder()
    .withUsername("testuser")
    .withPassword("testpass")
    .build();
```

### 3. Scenario Organization

```gherkin
Feature: User Authentication

  Background:
    Given I have access to the authentication API
    And I am not authenticated

  Scenario: Successful login with valid credentials
    Given I have valid user credentials
    When I attempt to login
    Then I should receive a successful response
    And the response should contain an access token

  Scenario: Failed login with invalid credentials
    Given I have invalid user credentials
    When I attempt to login
    Then I should receive an error response
    And the error message should indicate invalid credentials

  Scenario Outline: Login with different user types
    Given I have credentials for a <userType> user
    When I attempt to login
    Then I should receive a successful response
    And the response should contain appropriate permissions

    Examples:
      | userType |
      | admin    |
      | regular  |
      | guest    |
```

## Request Management

### 1. Template Design

```json
{
  "headers": {
    "Content-Type": "application/json",
    "Accept": "application/json",
    "Authorization": "Bearer {{token}}"
  },
  "body": {
    "name": "{{name}}",
    "email": "{{email}}",
    "role": "{{role}}"
  }
}
```

### 2. Template Variables

- Use descriptive variable names
- Use consistent naming conventions
- Document all variables

```java
// Good: Clear variable names
@RequestTemplateFile("requests/user-create.json")
RequestSpecBuilder createUserRequest(String name, String email, String role);

// Avoid: Generic names
@RequestTemplateFile("requests/user-create.json")
RequestSpecBuilder createUserRequest(String param1, String param2, String param3);
```

### 3. Request Validation

```java
// Validate request parameters
public static Response createUser(String name, String email, String role) {
    // Validate inputs
    assertNotNull("Name is required", name);
    assertNotNull("Email is required", email);
    assertTrue("Email must be valid", isValidEmail(email));
    
    return given()
        .spec(AppRequests.getRequestFactory()
            .createUserRequest(name, email, role).build())
        .baseUri(AppConfig.getConfig().getBaseUrl())
        .when()
        .post("/users")
        .then()
        .extract().response();
}
```

## Response Validation

### 1. Validation Hierarchy

```java
// Validate in order of importance
ValidateGenericResponse.assertThat(response)
    .httpStatusCodeIs(200)                    // 1. Status code
    .matchesSchema("schemas/user-schema.json") // 2. Schema validation
    .validateJsonPathData("data.id", userId)   // 3. Business logic
    .containsValue("success");                 // 4. Content validation
```

### 2. Schema Validation

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "properties": {
    "id": {
      "type": "number",
      "minimum": 1
    },
    "name": {
      "type": "string",
      "minLength": 1,
      "maxLength": 100
    },
    "email": {
      "type": "string",
      "format": "email"
    },
    "createdAt": {
      "type": "string",
      "format": "date-time"
    }
  },
  "required": ["id", "name", "email", "createdAt"],
  "additionalProperties": false
}
```

### 3. Custom Validation

```java
// Create custom validation methods
public class ValidateUserResponse extends ValidateResponse<ValidateUserResponse> {
    
    public ValidateUserResponse hasValidEmail() {
        String email = response.jsonPath().getString("data.email");
        assertTrue("Email should be valid", isValidEmail(email));
        return this;
    }
    
    public ValidateUserResponse hasRequiredFields() {
        softAssertions.assertThat(response.jsonPath().getString("data.id")).isNotNull();
        softAssertions.assertThat(response.jsonPath().getString("data.name")).isNotNull();
        softAssertions.assertThat(response.jsonPath().getString("data.email")).isNotNull();
        return this;
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}
```

## Error Handling

### 1. Expected Errors

```java
@Then("I should receive an error response")
public void i_should_receive_an_error_response() {
    ValidateGenericResponse.assertThat(response)
        .httpStatusCodeIs(400)
        .validateJsonPathData("error.code", "INVALID_CREDENTIALS")
        .validateJsonPathData("error.message", "Invalid username or password");
}
```

### 2. Exception Handling

```java
public static Response getUserById(String userId) {
    try {
        return given()
            .baseUri(AppConfig.getConfig().getBaseUrl())
            .when()
            .get("/users/" + userId)
            .then()
            .extract().response();
    } catch (Exception e) {
        log.error("Failed to get user with ID: {}", userId, e);
        throw new ApiException("Failed to retrieve user", e);
    }
}
```

### 3. Logging

```java
@When("I attempt to login")
public void i_attempt_to_login() {
    log.info("Attempting login with username: {}", loginRequest.getUsername());
    
    try {
        response = UserApi.login(loginRequest);
        log.info("Login successful, status code: {}", response.getStatusCode());
    } catch (Exception e) {
        log.error("Login failed", e);
        throw e;
    }
}
```

## Performance Considerations

### 1. Test Execution

```xml
<!-- Enable parallel execution in TestNG -->
<suite name="API Test Suite" parallel="methods" thread-count="4">
    <test name="API Tests">
        <classes>
            <class name="org.aditi.api_automation.runner.TestNGParallelRunner"/>
        </classes>
    </test>
</suite>
```

### 2. Request Optimization

```java
// Use request specifications for common configurations
public class RequestSpecs {
    public static RequestSpecification getDefaultSpec() {
        return new RequestSpecBuilder()
            .setBaseUri(AppConfig.getConfig().getBaseUrl())
            .setContentType(ContentType.JSON)
            .addHeader("Accept", "application/json")
            .build();
    }
}

// Usage
public static Response getUserById(String userId) {
    return given()
        .spec(RequestSpecs.getDefaultSpec())
        .when()
        .get("/users/" + userId)
        .then()
        .extract().response();
}
```

### 3. Response Caching

```java
// Cache responses for expensive operations
private static final Map<String, Response> cache = new ConcurrentHashMap<>();

public static Response getCachedResponse(String key) {
    return cache.computeIfAbsent(key, k -> makeApiCall(k));
}
```

## Security Best Practices

### 1. Sensitive Data Handling

```java
// Never log sensitive data
@When("I attempt to login")
public void i_attempt_to_login() {
    log.info("Attempting login with username: {}", loginRequest.getUsername());
    // Don't log: log.info("Password: {}", loginRequest.getPassword());
    
    response = UserApi.login(loginRequest);
}
```

### 2. Environment Variables

```properties
# Use environment variables for sensitive data
AUTH_TOKEN=${AUTH_TOKEN}
API_KEY=${API_KEY}
DATABASE_PASSWORD=${DB_PASSWORD}
```

### 3. Input Validation

```java
// Validate all inputs
public static Response createUser(String name, String email) {
    // Sanitize inputs
    name = sanitizeInput(name);
    email = sanitizeInput(email);
    
    // Validate inputs
    assertTrue("Name must not be empty", !name.trim().isEmpty());
    assertTrue("Email must be valid", isValidEmail(email));
    
    return given()
        .spec(createUserRequest(name, email).build())
        .when()
        .post("/users")
        .then()
        .extract().response();
}

private static String sanitizeInput(String input) {
    return input != null ? input.trim() : "";
}
```

## Maintenance and Documentation

### 1. Code Comments

```java
/**
 * Creates a new user in the system.
 * 
 * @param name The user's full name (required, max 100 characters)
 * @param email The user's email address (required, must be valid format)
 * @return Response containing the created user details
 * @throws IllegalArgumentException if name or email is invalid
 * @throws ApiException if the API call fails
 */
public static Response createUser(String name, String email) {
    // Implementation
}
```

### 2. Feature Documentation

```gherkin
Feature: User Management
  As a system administrator
  I want to manage user accounts
  So that I can control access to the system

  Background:
    Given I have administrative privileges
    And I am authenticated as an admin user

  Scenario: Create a new user account
    Given I want to create a new user account
    When I provide valid user details
    And I submit the user creation request
    Then the user account should be created successfully
    And I should receive a confirmation message
```

### 3. Test Data Documentation

```java
/**
 * Test data for user authentication scenarios.
 * 
 * Valid credentials:
 * - Username: testuser, Password: testpass
 * - Username: admin, Password: admin123
 * 
 * Invalid credentials:
 * - Username: invalid, Password: invalid
 * - Username: "", Password: ""
 */
public class UserTestData {
    public static final UserLoginRequest VALID_USER = 
        new UserLoginRequest("testuser", "testpass");
    
    public static final UserLoginRequest INVALID_USER = 
        new UserLoginRequest("invalid", "invalid");
}
```

### 4. Configuration Documentation

```properties
# API Configuration
# Base URL for the API server
BASE_URL=https://api.example.com

# API version to use
API_VERSION=v1

# Request timeout in milliseconds
TIMEOUT=30000

# Maximum number of retry attempts
MAX_RETRIES=3

# Authentication token (set via environment variable)
AUTH_TOKEN=${AUTH_TOKEN}
```

## Continuous Improvement

### 1. Code Reviews

- Review all test code before merging
- Ensure adherence to naming conventions
- Verify proper error handling
- Check for security vulnerabilities

### 2. Test Metrics

- Track test execution time
- Monitor test success rates
- Measure code coverage
- Analyze test maintenance effort

### 3. Framework Updates

- Keep dependencies up to date
- Monitor for security vulnerabilities
- Evaluate new features and improvements
- Plan migration strategies

---

Following these best practices will help you create maintainable, reliable, and efficient API tests with the framework. Remember to adapt these practices to your specific project requirements and team preferences.
