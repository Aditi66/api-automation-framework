# API Reference

This document provides a comprehensive reference for all classes, methods, and annotations available in the API Automation Framework.

## Table of Contents

1. [Configuration Classes](#configuration-classes)
2. [API Classes](#api-classes)
3. [Request Management](#request-management)
4. [Validation Classes](#validation-classes)
5. [Annotations](#annotations)
6. [DTOs](#dtos)
7. [Exceptions](#exceptions)
8. [Utilities](#utilities)

## Configuration Classes

### AppConfig

**Package**: `org.aditi.api_automation.config`

Singleton class that provides access to the configuration factory.

#### Methods

```java
public static ConfigFactory getConfig()
```
Returns an instance of the configuration factory.

**Returns**: `ConfigFactory` - The configuration factory instance

**Example**:
```java
ConfigFactory config = AppConfig.getConfig();
String baseUrl = config.getBaseUrl();
```

### ConfigFactory

**Package**: `org.aditi.api_automation.config`

Interface defining configuration properties and methods for retrieving them.

#### Methods

```java
String getProperty(String key)
```
Retrieves a configuration property by its key.

**Parameters**:
- `key` (String) - The property key

**Returns**: `String` - The property value

**Example**:
```java
String value = config.getProperty("CUSTOM_PROPERTY");
```

#### Annotated Methods

```java
@ConfigProperty("BASE_URL")
String getBaseUrl()

@ConfigProperty("BASE_API_PATH")
String getBaseApiPath()

@ConfigProperty("REQRES_BASE_URL")
String getReqResBaseUrl()

@ConfigProperty("REQRES_BASE_API_PATH")
String getReqResBaseApiPath()

@ConfigProperty("REGISTER_PATH")
String getRegistrationPath()

@ConfigProperty("LOGIN_PATH")
String getLoginPath()

@ConfigProperty("LOGOUT_PATH")
String getLogoutPath()
```

### CustomConfigManager

**Package**: `org.aditi.api_automation.config`

Singleton class for loading and managing configuration properties.

#### Methods

```java
public static CustomConfigManager getInstance()
```
Returns the singleton instance of CustomConfigManager.

**Returns**: `CustomConfigManager` - The singleton instance

```java
public static String getProperty(String key)
```
Retrieves a property value by key.

**Parameters**:
- `key` (String) - The property key

**Returns**: `String` - The property value, or null if not found

## API Classes

### User

**Package**: `org.aditi.api_automation.api`

Example API class demonstrating user-related API operations.

#### Methods

```java
public static Response userLogin(UserLoginRequest userLoginRequest)
```
Performs user login operation.

**Parameters**:
- `userLoginRequest` (UserLoginRequest) - The login request object

**Returns**: `Response` - The REST Assured response

**Example**:
```java
UserLoginRequest request = new UserLoginRequest("username", "password");
Response response = User.userLogin(request);
```

## Request Management

### RequestFactory

**Package**: `org.aditi.api_automation.requests`

Interface for creating request specifications using JSON templates.

#### Methods

```java
@RequestTemplateFile("requests/login.json")
RequestSpecBuilder loginRequest(String username, String password)
```
Creates a login request specification using the login.json template.

**Parameters**:
- `username` (String) - The username
- `password` (String) - The password

**Returns**: `RequestSpecBuilder` - The REST Assured request specification builder

**Example**:
```java
RequestSpecBuilder spec = requestFactory.loginRequest("user", "pass");
```

### AppRequests

**Package**: `org.aditi.api_automation.requests`

Utility class for accessing the request factory.

#### Methods

```java
public static RequestFactory getRequestFactory()
```
Returns the request factory instance.

**Returns**: `RequestFactory` - The request factory instance

**Example**:
```java
RequestFactory factory = AppRequests.getRequestFactory();
```

## Validation Classes

### ValidateResponse

**Package**: `org.aditi.api_automation.asserts`

Abstract base class for response validation with fluent API.

#### Methods

```java
public SELF_TYPE httpStatusCodeIs(int statusCode)
```
Validates that the response has the specified HTTP status code.

**Parameters**:
- `statusCode` (int) - The expected HTTP status code

**Returns**: `SELF_TYPE` - Self reference for method chaining

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .httpStatusCodeIs(200);
```

```java
public SELF_TYPE containsValue(String value)
```
Validates that the response body contains the specified value.

**Parameters**:
- `value` (String) - The value to search for in the response body

**Returns**: `SELF_TYPE` - Self reference for method chaining

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .containsValue("success");
```

```java
public SELF_TYPE matchesSchema(String fileClassPath)
```
Validates that the response matches the specified JSON schema.

**Parameters**:
- `fileClassPath` (String) - The classpath path to the JSON schema file

**Returns**: `SELF_TYPE` - Self reference for method chaining

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .matchesSchema("response/schemas/user-schema.json");
```

```java
public SELF_TYPE validateJsonPathData(String jsonPath, Object expectedValue)
```
Validates that the JSON path expression returns the expected value.

**Parameters**:
- `jsonPath` (String) - The JSON path expression
- `expectedValue` (Object) - The expected value

**Returns**: `SELF_TYPE` - Self reference for method chaining

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .validateJsonPathData("data.id", 1)
    .validateJsonPathData("data.name", "John Doe");
```

```java
public void assertAll()
```
Executes all soft assertions and reports any failures.

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .httpStatusCodeIs(200)
    .containsValue("success")
    .assertAll();
```

### ValidateGenericResponse

**Package**: `org.aditi.api_automation.asserts`

Concrete implementation of ValidateResponse for generic response validation.

#### Methods

```java
public static ValidateGenericResponse assertThat(Response response)
```
Creates a new ValidateGenericResponse instance for the given response.

**Parameters**:
- `response` (Response) - The REST Assured response to validate

**Returns**: `ValidateGenericResponse` - A new validation instance

**Example**:
```java
ValidateGenericResponse.assertThat(response)
    .httpStatusCodeIs(200)
    .containsValue("success");
```

## Annotations

### @ConfigProperty

**Package**: `org.aditi.api_automation.annotations`

Annotation for marking configuration property methods.

#### Usage

```java
@ConfigProperty("PROPERTY_KEY")
String getPropertyMethod();
```

**Parameters**:
- `value` (String) - The configuration property key

**Example**:
```java
@ConfigProperty("BASE_URL")
String getBaseUrl();
```

### @RequestTemplateFile

**Package**: `org.aditi.api_automation.annotations`

Annotation for marking request template methods.

#### Usage

```java
@RequestTemplateFile("requests/template.json")
RequestSpecBuilder methodName();
```

**Parameters**:
- `value` (String) - The path to the JSON template file

**Example**:
```java
@RequestTemplateFile("requests/login.json")
RequestSpecBuilder loginRequest(String username, String password);
```

## DTOs

### UserLoginRequest

**Package**: `org.aditi.api_automation.dto`

Data transfer object for user login requests.

#### Fields

- `username` (String) - The username
- `password` (String) - The password

#### Constructors

```java
public UserLoginRequest()
```
Default constructor.

```java
public UserLoginRequest(String username, String password)
```
Parameterized constructor.

**Parameters**:
- `username` (String) - The username
- `password` (String) - The password

#### Methods

```java
public String getUsername()
```
Gets the username.

**Returns**: `String` - The username

```java
public void setUsername(String username)
```
Sets the username.

**Parameters**:
- `username` (String) - The username

```java
public String getPassword()
```
Gets the password.

**Returns**: `String` - The password

```java
public void setPassword(String password)
```
Sets the password.

**Parameters**:
- `password` (String) - The password

**Example**:
```java
UserLoginRequest request = new UserLoginRequest("john.doe", "password123");
```

## Exceptions

### PropertyNotFoundException

**Package**: `org.aditi.api_automation.exception`

Exception thrown when a configuration property is not found.

#### Constructors

```java
public PropertyNotFoundException(String message)
```
Creates a new PropertyNotFoundException with the specified message.

**Parameters**:
- `message` (String) - The error message

**Example**:
```java
throw new PropertyNotFoundException("Property not found for key: " + key);
```

## Utilities

### ConfigFactoryProxy

**Package**: `org.aditi.api_automation.handlers`

Dynamic proxy implementation for the ConfigFactory interface.

#### Methods

```java
public static ConfigFactory newInstance(CustomConfigManager configManager)
```
Creates a new ConfigFactory proxy instance.

**Parameters**:
- `configManager` (CustomConfigManager) - The configuration manager

**Returns**: `ConfigFactory` - A new proxy instance

**Example**:
```java
ConfigFactory proxy = ConfigFactoryProxy.newInstance(configManager);
```

### RequestFactoryInvocationHandler

**Package**: `org.aditi.api_automation.handlers`

Invocation handler for RequestFactory dynamic proxy.

#### Methods

```java
public static RequestFactory newInstance()
```
Creates a new RequestFactory proxy instance.

**Returns**: `RequestFactory` - A new proxy instance

**Example**:
```java
RequestFactory factory = RequestFactoryInvocationHandler.newInstance();
```

## Constants

### FrameworkConstants

**Package**: `org.aditi.api_automation.constants`

Constants used throughout the framework.

#### Fields

```java
public static final String CONFIG_FILE_NAME = "config.properties"
```
The default configuration file name.

```java
public static final String ENVIRONMENT_PROPERTY = "faasos_env"
```
The system property for specifying the environment.

```java
public static final String DEFAULT_ENVIRONMENT = "dev"
```
The default environment if none is specified.

## Quick Examples

### Complete API Test Example

```java
package org.aditi.api_automation.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.aditi.api_automation.api.User;
import org.aditi.api_automation.asserts.ValidateGenericResponse;
import org.aditi.api_automation.dto.UserLoginRequest;

public class UserSteps {
    
    private Response response;
    private UserLoginRequest loginRequest;
    
    @Given("I have valid user credentials")
    public void i_have_valid_user_credentials() {
        loginRequest = new UserLoginRequest("testuser", "testpass");
    }
    
    @When("I attempt to login")
    public void i_attempt_to_login() {
        response = User.userLogin(loginRequest);
    }
    
    @Then("I should receive a successful login response")
    public void i_should_receive_a_successful_login_response() {
        ValidateGenericResponse.assertThat(response)
            .httpStatusCodeIs(200)
            .matchesSchema("response/schemas/login-schema.json")
            .validateJsonPathData("token", "QpwL5tke4Pnpja7X4")
            .containsValue("success");
    }
}
```

### Configuration Example

```java
// ConfigFactory.java
@ConfigProperty("API_BASE_URL")
String getApiBaseUrl();

@ConfigProperty("AUTH_TOKEN")
String getAuthToken();

// api.properties
API_BASE_URL=https://api.example.com
AUTH_TOKEN=your-auth-token

// Usage
String baseUrl = AppConfig.getConfig().getApiBaseUrl();
String token = AppConfig.getConfig().getAuthToken();
```

### Request Template Example

```java
// RequestFactory.java
@RequestTemplateFile("requests/user-create.json")
RequestSpecBuilder createUserRequest(String name, String email);

// requests/user-create.json
{
  "headers": {
    "Content-Type": "application/json",
    "Authorization": "Bearer {{token}}"
  },
  "body": {
    "name": "{{name}}",
    "email": "{{email}}"
  }
}

// Usage
RequestSpecBuilder spec = AppRequests.getRequestFactory()
    .createUserRequest("John Doe", "john@example.com");
```

---

This API reference provides comprehensive information about all the classes, methods, and annotations available in the framework. For more detailed examples and usage patterns, refer to the main documentation and getting started guide.
