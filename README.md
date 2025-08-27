# api-automation-framework
This Repository holds the Automation framework for API which has been created using GenAI tools. This uses the request response schema validation with annotation support.

## Project Requirements
* Java 21
* Maven 3.6
* git-crypt
* Intellij or Eclipse

## Installation

To set up the API Automation Framework, clone the repository and navigate to the project directory:

```bash
git clone https://github.com/Aditi66/api-automation-framework.git
cd api-automation-framework
```

Build the project using Maven:

```bash
mvn clean install
```

## Usage

The framework uses REST Assured for API testing. To run tests, execute:

```bash
mvn test
```

## Project Structure

- `src/main/java`: Contains main Java source files.
- `src/main/resources`: Contains resource files.
- `src/test/java`: Contains test Java files.
- `src/test/resources`: Contains test resource files.


## Framework Architecture
### Key Components
#### Configuration Management
- `ConfigFactory.java`: Interface defining configuration properties and retrieves configuration properties from different sources.
- `ConfigFactoryProxy.java`: Provides a unified interface for accessing configuration properties.
- `CustomConfigManager.java`: Singleton for loading properties and manages configuration properties and provides access to different sources.
- `AppConfig.java`: Singleton that holds the configuration factory. Access point for configuration

#### API Layer
- Organized by domain in their individual .java files
    - Example: `src/main/java/org/aditi/api_automation/api/{{module}}/{{module}}Api.java`
- Each API class contains methods for specific endpoints
- Uses RestAssured for HTTP requests

#### Authentication
- [] TODO: `AuthFactory.java`: Manages user authentication
- Supports multiple user roles and permissions
- App-specific authentication via AppSource enum

#### Test Structure
- BDD approach with Cucumber
- Feature files in Gherkin syntax
- Step definition classes map to business logic
- Scenario context for sharing test data

#### Response Validation
- JSON schema validation
- Status code verification
- Business logic validation


## Contributing

Contributions are welcome! Please fork the repository and create a pull request.

## License

This project is licensed under the MIT License.
