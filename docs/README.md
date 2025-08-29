# API Automation Framework Documentation

Welcome to the API Automation Framework! This comprehensive guide provides an overview of the framework and quick reference information.

## 📋 Table of Contents

1. [Framework Overview](#framework-overview)
2. [Quick Start](#quick-start)
3. [Project Structure](#project-structure)
4. [Key Concepts](#key-concepts)
5. [Documentation Guide](#documentation-guide)

## 🏗️ Framework Overview

The API Automation Framework is built with modern Java technologies and follows industry best practices:

### Key Features
- **BDD Testing**: Cucumber-based behavior-driven development
- **Dynamic Configuration**: Runtime configuration management with proxy patterns
- **Template-based Requests**: JSON templates for request specifications
- **Schema Validation**: JSON schema validation for response contracts
- **Fluent Assertions**: Chainable validation methods
- **Multi-Environment Support**: Environment-specific configurations

### Technology Stack
- **Java 21**: Latest LTS version with modern features
- **REST Assured 5.5.0**: REST API testing library
- **Cucumber 7.20.1**: BDD testing framework
- **TestNG 7.10.2**: Test execution and parallelization
- **AssertJ 3.27.0**: Fluent assertion library
- **Lombok**: Boilerplate code reduction
- **Jackson**: JSON processing

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Git
- IDE (IntelliJ IDEA or Eclipse recommended)

### Installation & First Test

```bash
# Clone and build
git clone https://github.com/Aditi66/api-automation-framework.git
cd api-automation-framework
mvn clean install

# Run tests
mvn test
```

**For detailed setup instructions, see the [Getting Started Guide](GETTING_STARTED.md).**

## 📁 Project Structure

```
api-automation-framework/
├── src/
│   ├── main/
│   │   ├── java/org/aditi/api_automation/
│   │   │   ├── annotations/          # Custom annotations
│   │   │   ├── api/                  # API domain classes
│   │   │   ├── config/               # Configuration management
│   │   │   ├── dto/                  # Data transfer objects
│   │   │   ├── handlers/             # Dynamic proxy handlers
│   │   │   ├── requests/             # Request factories
│   │   │   ├── specs/                # REST Assured specifications
│   │   │   └── utils/                # Utility classes
│   │   └── resources/
│   │       ├── environment/          # Environment-specific configs
│   │       ├── requests/             # JSON request templates
│   │       └── response/schemas/     # JSON schema files
│   └── test/
│       ├── java/org/aditi/api_automation/
│       │   ├── asserts/              # Validation classes
│       │   ├── runner/               # Test runners
│       │   └── steps/                # Cucumber step definitions
│       └── resources/
│           └── features/             # Cucumber feature files
├── docs/                             # Documentation
├── pom.xml                          # Maven configuration
└── README.md                        # Project overview
```

## 🔑 Key Concepts

### Configuration Management
The framework uses dynamic proxies and annotations for configuration management:

```java
// Access configuration
String baseUrl = AppConfig.getConfig().getBaseUrl();

// Add new properties
@ConfigProperty("NEW_PROPERTY")
String getNewProperty();
```

### API Testing Pattern
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

### Request Templates
Use JSON templates for request specifications:

```java
@RequestTemplateFile("requests/login.json")
RequestSpecBuilder loginRequest(String username, String password);
```

### Response Validation
Use fluent validation methods:

```java
ValidateGenericResponse.assertThat(response)
    .httpStatusCodeIs(200)
    .matchesSchema("response/schemas/user-schema.json")
    .validateJsonPathData("data.id", 1)
    .containsValue("success");
```

## 📚 Documentation Guide

### For New Users
1. **[Getting Started Guide](GETTING_STARTED.md)** - Complete setup and first test
2. **[Best Practices Guide](BEST_PRACTICES.md)** - Coding standards and patterns
3. **[Troubleshooting Guide](TROUBLESHOOTING.md)** - Common issues and solutions

### For Developers
1. **[API Reference](API_REFERENCE.md)** - Complete class and method documentation
2. **[Best Practices Guide](BEST_PRACTICES.md)** - Advanced patterns and techniques
3. **[Troubleshooting Guide](TROUBLESHOOTING.md)** - Debugging and optimization

### Quick Reference
- **[Documentation Index](INDEX.md)** - Find what you need quickly
- **[API Reference](API_REFERENCE.md)** - All classes and methods
- **[Configuration Guide](GETTING_STARTED.md#step-7-configuration-management)** - Environment setup

## 🎯 Common Use Cases

### Creating a New API Test
1. Create API class in `src/main/java/org/aditi/api_automation/api/`
2. Create feature file in `src/test/resources/features/`
3. Create step definitions in `src/test/java/org/aditi/api_automation/steps/`
4. Add request templates in `src/main/resources/requests/`
5. Add JSON schemas in `src/main/resources/response/schemas/`

### Adding Configuration Properties
1. Add method to `ConfigFactory` interface with `@ConfigProperty` annotation
2. Add property to appropriate environment file
3. Use `AppConfig.getConfig().getPropertyName()` in your code

### Creating Request Templates
1. Create JSON template in `src/main/resources/requests/`
2. Add method to `RequestFactory` with `@RequestTemplateFile` annotation
3. Use template in API class with `AppRequests.getRequestFactory()`

## 🔧 Troubleshooting

### Common Issues
- **PropertyNotFoundException**: Check property definition and annotation
- **FileNotFoundException**: Verify template file exists and path is correct
- **Schema validation failed**: Update JSON schema to match response structure
- **Tests not found**: Check feature file location and step definition package

### Getting Help
1. Check the [Troubleshooting Guide](TROUBLESHOOTING.md)
2. Review [Best Practices](BEST_PRACTICES.md)
3. Search existing [GitHub Issues](https://github.com/Aditi66/api-automation-framework/issues)
4. Create a new issue with detailed information

## 🤝 Contributing

Contributions are welcome! Please see our [Contributing Guide](INDEX.md#contributing-to-documentation) for details.

### Contributing to Documentation
We welcome contributions to improve the documentation! Please see our [Documentation Contributing Guide](INDEX.md#contributing-to-documentation) for details.

## 📄 License

This project is licensed under the MIT License.

---

**Happy Testing! 🚀**

For detailed information, start with the [Documentation Index](INDEX.md) to find what you need.
