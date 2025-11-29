# SUATC Automation Framework

## 🚀 Overview

A hybrid **UI + API** test automation framework designed for scalability and maintainability.

Built with **Java 17**, **Selenium 4**, **TestNG**, **Cucumber**, and **REST Assured**, the framework follows the **Page
Object Model (POM)** pattern to keep page structure and test logic cleanly separated.

The goal of this project is to demonstrate production-style framework design suitable for real-world SDET roles.

- **UI under test:** SauceDemo – https://www.saucedemo.com
- **API under test:** Reqres – https://reqres.in (secured with API key)

---

## 🛠 Tech Stack

- **Language:** Java 17
- **UI Automation:** Selenium 4.27.0
- **API Automation:** REST Assured 5.5.6, Jackson Databind 2.20.1
- **Test Frameworks:** TestNG 7.10.2, Cucumber 7.20.1 (TestNG runner)
- **Build Tool:** Maven
- **Logging:** Log4j2
    - Console + rolling file logs under `target/logs`
    - Consistent logger usage across core classes, pages, listeners, and Cucumber hooks
- **Reporting:**
    - TestNG console output
    - Cucumber HTML reports via `maven-cucumber-reporting` *(planned)*
    - Allure integration *(planned)*

---

## ⚙️ Configuration

Runtime settings are controlled primarily through:

- `src/main/resources/config.properties`
- `src/main/resources/log4j2.xml`

### `config.properties`

This file defines browser, environment, timeouts, URLs, API auth, and UI test data. Key entries:

- **Browser / Environment**
    - `browser` – e.g. `chrome`
    - `browser.headless` – `true` / `false` to run in headless mode
    - `env` – current environment (e.g. `qa`, `prod`)

- **Application URLs**
    - `url.qa` – base UI URL for QA (SauceDemo)
    - `url.prod` – base UI URL for Prod (SauceDemo)
    - `url.api` – base API URL (Reqres), independent of `env`

- **API Auth**
    - `api.key` – API key for Reqres, used as `x-api-key` header for all API tests

- **Timeouts (seconds)**
    - `timeout.implicit` – implicit wait (set to `0` to avoid conflicts with explicit waits)
    - `timeout.default` – default explicit wait timeout
    - `timeout.page_load` – page load timeout

- **Reporting / Failure Handling**
    - `screenshot.on_failure` – enables/disables automatic screenshot capture via `TestListener` on TestNG test failures

- **SauceDemo User Credentials (UI)**
    - `user.standard` / `pass.standard` – standard UI user
    - `user.locked_out` / `pass.locked_out` – locked-out user (negative login scenarios)

> Note: Credentials in this file are **public test users only**. Real projects should load secrets via environment
> variables or CI/CD secret management.

### `log4j2.xml`

Located at `src/main/resources/log4j2.xml`, this file configures framework logging:

- Console logging with timestamp, thread, level, and logger name
- Rolling file logging under `target/logs/automation.log`
- Log rotation based on size and date, with a limited number of retained files
- Noise reduction for verbose third-party libraries (HTTP clients, drivers, etc.)

Log output is written under `target/logs/` and is ignored by Git via `.gitignore`.

---

## ✅ How to Run

### Prerequisites

- Java 17 installed and on your `PATH`
- Maven installed (`mvn -v` should work in your terminal)

### Run the Full TestNG Suite

From the project root (where `pom.xml` and `testng.xml` are located), run:

```bash
mvn clean test
```

This executes:

- UI tests (TestNG)
- API tests (TestNG + REST Assured)
- BDD tests (Cucumber feature(s) via TestNG runner)

---

## 🧪 Test Coverage (Current)

### UI (Selenium + TestNG, POM)

- **Login flow (SauceDemo)**
    - `LoginTest`:
        - Positive login with a valid standard user
        - Negative login scenarios using TestNG `@DataProvider`:
            - Wrong password
            - Locked-out user
            - Missing username
            - Missing password
    - Page Objects:
        - `LoginPage` – login form interactions
        - `InventoryPage` – verification of the products page
- **Checkout flow (SauceDemo)**
    - `CheckoutTest`:
        - Logs in as a standard user
        - Adds Sauce Labs Backpack to the cart
        - Proceeds through the checkout information step to the overview page
        - Asserts the selected item is present on the checkout overview page
    - Page Objects:
        - `InventoryPage` – adds product to cart and opens the cart
        - `CartPage` – verifies cart contents and navigates to checkout
        - `CheckoutInformationPage` – enters customer details and continues
        - `CheckoutOverviewPage` – verifies item is present before order completion

UI tests extend `BaseTest`, which manages WebDriver lifecycle via `DriverFactory` and integrates with `TestListener` for
logging and screenshots on failure.

### API (REST Assured + TestNG)

- **User API tests against Reqres**
    - `UserApiTest` (extends `BaseApiTest`):
        - `shouldReturnUserList` – validates a list of users with non-empty `data`
        - `shouldCreateUser` – POSTs a JSON payload, verifies `201` and response body (`name`, `job`, `id`, `createdAt`)
        - `shouldReturn404ForUnknownUser` – verifies `404` for a non-existing user

All API tests use:

- `BaseApiTest` to set:
    - `RestAssured.baseURI` from `url.api`
    - Default `requestSpecification` with `x-api-key` header from `api.key`

### BDD (Cucumber + TestNG)

- **Login feature (SauceDemo)**
    - `login.feature`:
        - *Scenario: Successful login with valid credentials*
            - `Given I am on the login page`
            - `When I log in as a standard user`
            - `Then I should see the products page`
    - Step definitions:
        - `LoginSteps` – reuses `LoginPage` / `InventoryPage` and `ConfigReader` to perform the login and assert the
          products page
    - Hooks:
        - `Hooks` – Cucumber `@Before` / `@After` that initialize and quit WebDriver via `DriverFactory` per scenario

Cucumber scenarios are executed via a TestNG-based `CucumberTestRunner`.

---

## 🏗 Project Layout

**Current structure:**

- `src/main/java/com/suatc/qa/base`
    - Core base classes (e.g. `BasePage`)
- `src/main/java/com/suatc/qa/config`
    - Configuration (`ConfigReader`)
- `src/main/java/com/suatc/qa/factory`
    - WebDriver management (`DriverFactory`)
- `src/main/java/com/suatc/qa/exceptions`
    - Custom exceptions (e.g. `UiElementTimeoutException`)
- `src/main/java/com/suatc/qa/listeners`
    - TestNG listeners (e.g. `TestListener` for screenshots, logging)
- `src/main/java/com/suatc/qa/pages`
    - UI Page Objects (`LoginPage`, `InventoryPage`, `CartPage`, `CheckoutInformationPage`, `CheckoutOverviewPage`)
- `src/main/resources`
    - Framework configuration (`config.properties`, `log4j2.xml`)
- `src/test/java/com/suatc/qa/base`
    - Test base classes:
        - `BaseTest` – UI TestNG base (WebDriver lifecycle)
        - `BaseApiTest` – API TestNG base (REST Assured base URI + API key)
- `src/test/java/com/suatc/qa/tests/ui`
    - UI TestNG tests (e.g. `LoginTest`, `CheckoutTest`)
- `src/test/java/com/suatc/qa/tests/api`
    - API TestNG tests (e.g. `UserApiTest`)
- `src/test/java/com/suatc/qa/stepdefinitions`
    - Cucumber step definitions (`LoginSteps`)
    - Cucumber hooks (`Hooks` – WebDriver setup/teardown)
- `src/test/java/com/suatc/qa/runners`
    - Cucumber + TestNG runners (e.g. `CucumberTestRunner`)
- `src/test/resources/features`
    - Cucumber feature files (e.g. `login.feature`)
- `testng.xml`
    - TestNG suite entry point (UI tests, API tests, and Cucumber runner)
- `pom.xml`
    - Maven configuration and dependencies

---

## 📌 Planned Enhancements

- Additional UI flows (e.g. remove-from-cart, sorting/filtering, full order completion)
- More API test coverage and reusable API client utilities
- Cucumber HTML report generation via `maven-cucumber-reporting`
- Allure integration for combined UI/API/BDD reporting
- Shared utility layer under `src/main/java/com/suatc/qa/utils` (e.g. data generators, common matchers)
