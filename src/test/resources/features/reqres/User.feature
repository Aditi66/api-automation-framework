
Feature: User

  Scenario: Single User Registration
    Given I want to register a new user
    When I send a POST request to register api
    Then I should receive a successful response

  Scenario Outline: Multiple User Registration
    Given I want to register a new user with <username> and <password>
    When I send a POST request to register api
    Then I should receive a successful response

    Examples:
      | username | password |
      | aditi    | 123456   |
      | Krati    | 1234567  |
      | Dhruv    | 12345678 |
      | Vaishali | 123456789|