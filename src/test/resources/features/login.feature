Feature: Login

  As a standard SauceDemo user
  I want to log into the application
  So that I can view the products page

  Scenario: Successful login with valid credentials
    Given I am on the login page
    When I log in as a standard user
    Then I should see the products page
