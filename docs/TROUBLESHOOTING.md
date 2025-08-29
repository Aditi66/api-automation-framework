# Troubleshooting

This guide helps you identify and resolve common issues when using the API Automation Framework.

## Common Error Messages

### 1. PropertyNotFoundException

**Error Message**:
```
PropertyNotFoundException: Property not found for key: BASE_URL
```

**Causes**:
- Property not defined in configuration files
- Incorrect property name in `@ConfigProperty` annotation
- Environment-specific property file missing
- Typo in property name

**Solutions**:

1. **Check property definition**:
   ```properties
   # src/main/resources/api.properties
   BASE_URL=https://api.example.com
   ```

2. **Verify annotation usage**:
   ```java
   @ConfigProperty("BASE_URL")  // Must match property name exactly
   String getBaseUrl();
   ```

3. **Check environment configuration**:
   ```bash
   # Ensure environment property is set
   mvn test -Dfaasos_env=dev
   ```

4. **Verify file location**:
   ```
   src/main/resources/
   ├── api.properties
   └── environment/
       ├── dev.properties
       └── prod.properties
   ```

### 2. FileNotFoundException

**Error Message**:
```
FileNotFoundException: requests/login.json
```

**Causes**:
- Template file doesn't exist
- Incorrect file path in `@RequestTemplateFile` annotation
- File not in the correct resources directory

**Solutions**:

1. **Verify file exists**:
   ```
   src/main/resources/requests/login.json
   ```

2. **Check annotation path**:
   ```java
   @RequestTemplateFile("requests/login.json")  // Relative to resources
   RequestSpecBuilder loginRequest(String username, String password);
   ```

3. **Ensure proper file structure**:
   ```
   src/main/resources/
   └── requests/
       ├── login.json
       ├── user-create.json
       └── user-update.json
   ```

### 3. Schema Validation Errors

**Error Message**:
```
Schema validation failed: [path] is not a valid email
```

**Causes**:
- JSON schema doesn't match actual response structure
- Schema validation rules too strict
- Response format changed

**Solutions**:

1. **Update schema to match response**:
   ```json
   {
     "$schema": "http://json-schema.org/draft-07/schema#",
     "type": "object",
     "properties": {
       "email": {
         "type": "string",
         "format": "email"
       }
     }
   }
   ```

2. **Use schema generator**:
   ```bash
   # Use online tools to generate schema from actual response
   # https://transform.tools/json-to-json-schema
   ```

3. **Temporarily disable schema validation**:
   ```java
   // Comment out schema validation for debugging
   // .matchesSchema("response/schemas/user-schema.json")
   ```

### 4. TestNG Configuration Errors

**Error Message**:
```
No tests found
```

**Causes**:
- Feature files not in correct location
- Step definitions not properly linked
- Test runner configuration issues

**Solutions**:

1. **Check feature file location**:
   ```
   src/test/resources/features/
   ```

2. **Verify step definition package**:
   ```java
   package org.aditi.api_automation.steps;
   
   @CucumberOptions(
       features = "src/test/resources/features",
       glue = "org.aditi.api_automation.steps"
   )
   ```

3. **Check TestNG XML configuration**:
   ```xml
   <suite name="API Test Suite">
       <test name="API Tests">
           <classes>
               <class name="org.aditi.api_automation.runner.TestNGParallelRunner"/>
           </classes>
       </test>
   </suite>
   ```

## Configuration Issues

### 1. Environment Not Loading

**Problem**: Configuration properties not loading for specific environment.

**Debug Steps**:

1. **Check environment variable**:
   ```bash
   echo $faasos_env
   ```

2. **Verify environment file exists**:
   ```
   src/main/resources/environment/dev.properties
   src/main/resources/environment/prod.properties
   ```

3. **Check CustomConfigManager**:
   ```java
   // Add debug logging
   log.info("Loading environment: {}", environment);
   log.info("Properties loaded: {}", properties);
   ```

**Solution**:
```bash
# Set environment explicitly
mvn test -Dfaasos_env=dev
```

### 2. Property Resolution Issues

**Problem**: Properties not resolving correctly.

**Debug Steps**:

1. **Check property hierarchy**:
   ```java
   // Properties are loaded in this order:
   // 1. System properties
   // 2. Environment-specific properties
   // 3. Default properties
   ```

2. **Add debug logging**:
   ```java
   log.info("Property value for {}: {}", key, value);
   ```

**Solution**:
```properties
# Use explicit property definition
BASE_URL=https://api.example.com
API_VERSION=v1
```

### 3. Dynamic Proxy Issues

**Problem**: `ConfigFactoryProxy` not working correctly.

**Debug Steps**:

1. **Check proxy creation**:
   ```java
   ConfigFactory proxy = ConfigFactoryProxy.newInstance(configManager);
   log.info("Proxy created: {}", proxy.getClass().getName());
   ```

2. **Verify method invocation**:
   ```java
   // Add debug logging in invoke method
   log.info("Method called: {}", method.getName());
   log.info("Property key: {}", propertyName);
   ```

**Solution**:
```java
// Ensure proper exception handling
try {
    String value = config.getBaseUrl();
} catch (PropertyNotFoundException e) {
    log.error("Property not found: {}", e.getMessage());
    // Handle gracefully
}
```

## Test Execution Problems

### 1. Cucumber Step Definition Issues

**Problem**: Step definitions not matching feature files.

**Debug Steps**:

1. **Check step definition syntax**:
   ```java
   @Given("I have valid user credentials")  // Must match exactly
   public void i_have_valid_user_credentials() {
       // Implementation
   }
   ```

2. **Verify parameter types**:
   ```java
   @When("I request user with ID {string}")  // Use {string} for strings
   public void i_request_user_with_id(String id) {
       // Implementation
   }
   ```

3. **Check package scanning**:
   ```java
   @CucumberOptions(
       features = "src/test/resources/features",
       glue = "org.aditi.api_automation.steps"  // Must include step package
   )
   ```

**Solution**:
```java
// Use proper Cucumber annotations
@Given("I have valid user credentials")
@When("I attempt to login")
@Then("I should receive a successful response")
```

### 2. Test Data Issues

**Problem**: Test data not available or incorrect.

**Debug Steps**:

1. **Check test data setup**:
   ```java
   @Before
   public void setupTestData() {
       // Ensure test data is properly initialized
       log.info("Setting up test data");
   }
   ```

2. **Verify data cleanup**:
   ```java
   @After
   public void cleanupTestData() {
       // Clean up test data after each test
       log.info("Cleaning up test data");
   }
   ```

**Solution**:
```java
// Use proper test data management
public class TestDataManager {
    public static UserLoginRequest getValidUser() {
        return new UserLoginRequest("testuser", "testpass");
    }
    
    public static UserLoginRequest getInvalidUser() {
        return new UserLoginRequest("invalid", "invalid");
    }
}
```

### 3. Parallel Execution Issues

**Problem**: Tests failing when run in parallel.

**Debug Steps**:

1. **Check thread safety**:
   ```java
   // Ensure shared state is thread-safe
   private static final ThreadLocal<Response> responseHolder = new ThreadLocal<>();
   ```

2. **Verify resource isolation**:
   ```java
   @Before
   public void setup() {
       // Reset state for each test
       responseHolder.remove();
   }
   ```

**Solution**:
```java
// Use thread-local variables for shared state
public class TestContext {
    private static final ThreadLocal<Response> response = new ThreadLocal<>();
    private static final ThreadLocal<String> userId = new ThreadLocal<>();
    
    public static void setResponse(Response response) {
        TestContext.response.set(response);
    }
    
    public static Response getResponse() {
        return TestContext.response.get();
    }
}
```

## Request/Response Issues

### 1. HTTP Connection Errors

**Error Message**:
```
java.net.ConnectException: Connection refused
```

**Causes**:
- API server not running
- Incorrect base URL
- Network connectivity issues
- Firewall blocking requests

**Solutions**:

1. **Verify API server status**:
   ```bash
   curl -I https://api.example.com/health
   ```

2. **Check base URL configuration**:
   ```properties
   BASE_URL=https://api.example.com
   ```

3. **Test connectivity**:
   ```java
   // Add connectivity test
   @BeforeClass
   public static void testConnectivity() {
       try {
           given().baseUri(AppConfig.getConfig().getBaseUrl())
               .when().get("/health")
               .then().statusCode(200);
       } catch (Exception e) {
           log.error("API server not accessible: {}", e.getMessage());
           throw new RuntimeException("API server not accessible", e);
       }
   }
   ```

### 2. Authentication Issues

**Error Message**:
```
401 Unauthorized
```

**Causes**:
- Missing authentication token
- Invalid credentials
- Expired token
- Incorrect authentication method

**Solutions**:

1. **Check authentication setup**:
   ```java
   // Ensure proper authentication
   given()
       .header("Authorization", "Bearer " + getAuthToken())
       .when()
       .get("/protected-endpoint")
   ```

2. **Verify token generation**:
   ```java
   public static String getAuthToken() {
       // Implement token generation/retrieval
       return AppConfig.getConfig().getAuthToken();
   }
   ```

3. **Add token refresh logic**:
   ```java
   public static String refreshTokenIfNeeded() {
       // Check if token is expired and refresh if needed
       return tokenManager.refreshToken();
   }
   ```

### 3. Request Timeout Issues

**Error Message**:
```
java.net.SocketTimeoutException: Read timed out
```

**Causes**:
- Slow API response
- Network latency
- Insufficient timeout configuration

**Solutions**:

1. **Increase timeout**:
   ```java
   given()
       .config(RestAssured.config()
           .httpClient(HttpClientConfig.httpClientConfig()
               .setConnectTimeout(30000)
               .setReadTimeout(60000)))
       .when()
       .get("/slow-endpoint")
   ```

2. **Configure timeout in properties**:
   ```properties
   REQUEST_TIMEOUT=60000
   CONNECT_TIMEOUT=30000
   ```

3. **Add retry logic**:
   ```java
   public static Response getWithRetry(String endpoint, int maxRetries) {
       for (int i = 0; i < maxRetries; i++) {
           try {
               return given().when().get(endpoint);
           } catch (Exception e) {
               if (i == maxRetries - 1) throw e;
               log.warn("Request failed, retrying... ({}/{})", i + 1, maxRetries);
           }
       }
       throw new RuntimeException("Max retries exceeded");
   }
   ```

## Validation Problems

### 1. JSON Path Validation Failures

**Error Message**:
```
Expected: "John Doe"
Actual: "Jane Smith"
```

**Causes**:
- Incorrect JSON path expression
- Response structure changed
- Case sensitivity issues
- Data type mismatches

**Solutions**:

1. **Verify JSON path expression**:
   ```java
   // Use correct JSON path syntax
   .validateJsonPathData("data.name", "John Doe")  // Correct
   .validateJsonPathData("data.Name", "John Doe")  // Wrong case
   ```

2. **Debug response structure**:
   ```java
   // Print response for debugging
   log.info("Response body: {}", response.getBody().asString());
   log.info("JSON path value: {}", response.jsonPath().getString("data.name"));
   ```

3. **Use flexible validation**:
   ```java
   // Use contains instead of exact match
   .validateJsonPathData("data.name", containsString("John"))
   ```

### 2. Schema Validation Failures

**Error Message**:
```
Schema validation failed: [path] is not a valid email
```

**Solutions**:

1. **Update schema to be more flexible**:
   ```json
   {
     "properties": {
       "email": {
         "type": "string",
         "pattern": "^[^@]+@[^@]+\\.[^@]+$"
       }
     }
   }
   ```

2. **Use schema validation tools**:
   ```bash
   # Validate schema syntax
   npm install -g ajv-cli
   ajv validate -s schema.json -d response.json
   ```

3. **Temporarily disable schema validation**:
   ```java
   // Comment out for debugging
   // .matchesSchema("response/schemas/user-schema.json")
   ```

### 3. Status Code Validation Issues

**Error Message**:
```
Expected: 200
Actual: 201
```

**Causes**:
- API behavior changed
- Different HTTP methods return different status codes
- Conditional logic in API

**Solutions**:

1. **Use appropriate status codes**:
   ```java
   // POST requests typically return 201 for creation
   .httpStatusCodeIs(201)  // For resource creation
   .httpStatusCodeIs(200)  // For successful operations
   ```

2. **Use status code ranges**:
   ```java
   // Validate status code is in success range
   .httpStatusCodeIs(both(greaterThanOrEqualTo(200)).and(lessThan(300)))
   ```

3. **Add conditional validation**:
   ```java
   if (response.getStatusCode() == 200 || response.getStatusCode() == 201) {
       // Both are acceptable
   } else {
       fail("Unexpected status code: " + response.getStatusCode());
   }
   ```

## Performance Issues

### 1. Slow Test Execution

**Problem**: Tests taking too long to execute.

**Solutions**:

1. **Enable parallel execution**:
   ```xml
   <suite name="API Test Suite" parallel="methods" thread-count="4">
   ```

2. **Optimize request specifications**:
   ```java
   // Use shared request specifications
   public static RequestSpecification getDefaultSpec() {
       return new RequestSpecBuilder()
           .setBaseUri(AppConfig.getConfig().getBaseUrl())
           .setContentType(ContentType.JSON)
           .build();
   }
   ```

3. **Implement response caching**:
   ```java
   private static final Map<String, Response> cache = new ConcurrentHashMap<>();
   
   public static Response getCachedResponse(String key) {
       return cache.computeIfAbsent(key, k -> makeApiCall(k));
   }
   ```

### 2. Memory Issues

**Problem**: Tests consuming too much memory.

**Solutions**:

1. **Clean up resources**:
   ```java
   @After
   public void cleanup() {
       // Clear caches
       cache.clear();
       // Reset thread locals
       TestContext.clear();
   }
   ```

2. **Limit response size**:
   ```java
   given()
       .queryParam("limit", 10)  // Limit response size
       .when()
       .get("/users")
   ```

3. **Use streaming for large responses**:
   ```java
   given()
       .when()
       .get("/large-dataset")
       .then()
       .body(hasSize(lessThan(1000)));  // Validate reasonable size
   ```

## Environment-Specific Issues

### 1. Development vs Production

**Problem**: Tests work in dev but fail in production.

**Solutions**:

1. **Environment-specific configuration**:
   ```properties
   # dev.properties
   BASE_URL=https://dev-api.example.com
   TIMEOUT=30000
   
   # prod.properties
   BASE_URL=https://api.example.com
   TIMEOUT=60000
   ```

2. **Environment-specific test data**:
   ```java
   public static UserLoginRequest getTestUser() {
       String env = System.getProperty("faasos_env", "dev");
       if ("prod".equals(env)) {
           return new UserLoginRequest("prod-user", "prod-pass");
       } else {
           return new UserLoginRequest("dev-user", "dev-pass");
       }
   }
   ```

3. **Conditional test execution**:
   ```java
   @Test
   @Ignore("Only run in production")
   public void testProductionSpecificFeature() {
       assumeTrue("prod".equals(System.getProperty("faasos_env")));
       // Test implementation
   }
   ```

### 2. CI/CD Pipeline Issues

**Problem**: Tests fail in CI/CD but pass locally.

**Solutions**:

1. **Check environment variables**:
   ```yaml
   # .github/workflows/test.yml
   env:
     faasos_env: dev
     JAVA_OPTS: -Xmx2g
   ```

2. **Add CI-specific configuration**:
   ```properties
   # ci.properties
   BASE_URL=https://ci-api.example.com
   TIMEOUT=120000
   ```

3. **Use Docker for consistent environment**:
   ```dockerfile
   FROM openjdk:21-jdk
   COPY . /app
   WORKDIR /app
   RUN mvn clean install
   CMD ["mvn", "test"]
   ```

## Debugging Techniques

### 1. Enable Debug Logging

```java
// Add to test class
@Before
public void enableDebugLogging() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.config = RestAssured.config()
        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
}
```

### 2. Add Custom Logging

```java
@When("I attempt to login")
public void i_attempt_to_login() {
    log.info("Attempting login with username: {}", loginRequest.getUsername());
    log.info("Request body: {}", loginRequest);
    
    response = UserApi.login(loginRequest);
    
    log.info("Response status: {}", response.getStatusCode());
    log.info("Response body: {}", response.getBody().asString());
}
```

### 3. Use Test Listeners

```java
public class TestListener implements ITestListener {
    @Override
    public void onTestStart(ITestResult result) {
        log.info("Starting test: {}", result.getName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {}", result.getName());
        log.error("Exception: {}", result.getThrowable());
    }
}
```

### 4. Create Debug Utilities

```java
public class DebugUtils {
    public static void printResponseDetails(Response response) {
        log.info("Status Code: {}", response.getStatusCode());
        log.info("Headers: {}", response.getHeaders());
        log.info("Body: {}", response.getBody().asString());
        log.info("Time: {}", response.getTime());
    }
    
    public static void saveResponseToFile(Response response, String filename) {
        try {
            Files.write(Paths.get(filename), response.getBody().asString().getBytes());
            log.info("Response saved to: {}", filename);
        } catch (IOException e) {
            log.error("Failed to save response", e);
        }
    }
}
```

## Getting Help

### 1. Check Logs

```bash
# Enable verbose logging
mvn test -Dlogback.configurationFile=logback-debug.xml

# Check Maven logs
mvn test -X

# Check TestNG reports
open target/surefire-reports/index.html
```

### 2. Use Debug Mode

```java
// Add debug breakpoints in IDE
// Use conditional breakpoints for specific scenarios
// Step through code execution
```

### 3. Create Minimal Reproduction

```java
// Create a minimal test case that reproduces the issue
@Test
public void minimalReproduction() {
    // Minimal setup
    // Minimal request
    // Minimal validation
}
```

### 4. Community Resources

- **GitHub Issues**: Check existing issues and create new ones
- **Stack Overflow**: Search for similar problems
- **Framework Documentation**: Review official documentation
- **Team Knowledge Base**: Check internal documentation

### 5. Contact Support

When contacting support, include:

1. **Error message**: Complete error stack trace
2. **Environment details**: OS, Java version, Maven version
3. **Configuration**: Relevant configuration files
4. **Test code**: Minimal reproduction case
5. **Expected vs actual behavior**: Clear description of the issue

---

This troubleshooting guide should help you resolve most common issues. If you encounter a problem not covered here, please create a detailed issue report with all the information mentioned in the "Getting Help" section.
