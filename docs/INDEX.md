---
layout: home
hero:
  name: API Automation Framework
  text: A comprehensive framework for API testing
  tagline: Built with REST Assured, Cucumber, and TestNG for modern API testing
  actions:
    - theme: brand
      text: Get Started
      link: /getting-started
    - theme: alt
      text: View on GitHub
      link: https://github.com/Aditi66/api-automation-framework
    - theme: alt
      text: API Reference
      link: /api-reference

features:
  - icon: 🚀
    title: Fast & Reliable
    details: Built with modern Java technologies for fast and reliable API testing
  - icon: 🎯
    title: BDD Approach
    details: Use Cucumber for behavior-driven development with clear, readable test scenarios
  - icon: ⚙️
    title: Dynamic Configuration
    details: Runtime configuration management with proxy patterns and environment support
  - icon: 📝
    title: Template-based Requests
    details: JSON templates for request specifications with variable substitution
  - icon: ✅
    title: Schema Validation
    details: JSON schema validation for response contracts and data integrity
  - icon: 🔧
    title: Fluent Assertions
    details: Chainable validation methods for comprehensive response checking
---

## Quick Start

Get up and running in minutes:

```bash
# Clone the repository
git clone https://github.com/Aditi66/api-automation-framework.git
cd api-automation-framework

# Build the project
mvn clean install

# Run tests
mvn test
```

## Key Features

- **BDD Testing**: Cucumber-based behavior-driven development
- **Dynamic Configuration**: Runtime configuration management with proxy patterns
- **Template-based Requests**: JSON templates for request specifications
- **Schema Validation**: JSON schema validation for response contracts
- **Fluent Assertions**: Chainable validation methods
- **Multi-Environment Support**: Environment-specific configurations

## Technology Stack

- **Java 21**: Latest LTS version with modern features
- **REST Assured 5.5.0**: REST API testing library
- **Cucumber 7.20.1**: BDD testing framework
- **TestNG 7.10.2**: Test execution and parallelization
- **AssertJ 3.27.0**: Fluent assertion library
- **Lombok**: Boilerplate code reduction
- **Jackson**: JSON processing

## Documentation

- **[Getting Started Guide](/getting-started)**: Complete setup and first test
- **[API Reference](/api-reference)**: Complete class and method documentation
- **[Best Practices](/best-practices)**: Coding standards and patterns
- **[Troubleshooting](/troubleshooting)**: Common issues and solutions

## Example Test

```java
@Given("I have valid user credentials")
public void i_have_valid_user_credentials() {
    loginRequest = new UserLoginRequest("testuser", "testpass");
}

@When("I attempt to login")
public void i_attempt_to_login() {
    response = UserApi.login(loginRequest);
}

@Then("I should receive a successful response")
public void i_should_receive_a_successful_response() {
    ValidateGenericResponse.assertThat(response)
        .httpStatusCodeIs(200)
        .matchesSchema("response/schemas/login-schema.json")
        .validateJsonPathData("token", "QpwL5tke4Pnpja7X4");
}
```

## Contributing

We welcome contributions! Please see our [Contributing Guide](https://github.com/Aditi66/api-automation-framework/blob/main/CONTRIBUTING.md) for details.

## License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/Aditi66/api-automation-framework/blob/main/LICENSE) file for details.
