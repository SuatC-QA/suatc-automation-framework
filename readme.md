# SUATC Automation Framework

## 🚀 Overview

A hybrid UI + API test automation framework designed for scalability and maintainability.

Built with **Java 17**, **Selenium 4**, **TestNG**, and **Cucumber**, the framework follows the **Page Object Model (POM)** pattern to keep page structure and test logic cleanly separated.

The goal of this project is to demonstrate production-style framework design suitable for real-world SDET roles.

---

## 🛠 Tech Stack

- **Language:** Java 17
- **UI Automation:** Selenium 4.27.0
- **API Automation:** REST Assured 5.5.6, Jackson Databind 2.20.1 *(dependencies added, API test module in progress)*
- **Test Frameworks:** TestNG 7.10.2, Cucumber 7.20.1 (TestNG runner)
- **Build Tool:** Maven
- **Logging:** Log4j2 (configuration in place for console + rolling file logs under `target/logs`; logging usage will be expanded over time)
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

This file defines browser, environment, timeouts, and test data. Example keys:

- `browser` - e.g. `chrome`
- `browser.headless` - `true` / `false` to run in headless mode
- `env` - current environment (e.g. `qa`, `prod`)
- `url.qa`, `url.prod` - base URLs per environment
- `timeout.implicit` - implicit wait in seconds (recommended `0` when relying on explicit waits)
- `timeout.default` - default explicit wait timeout (seconds)
- `timeout.page_load` - page load timeout (seconds)
- `screenshot.on_failure` - planned flag for screenshot behavior on test failure
- `user.*` / `pass.*` - public test credentials (e.g. SauceDemo users)

> Note: Credentials in this file are **public test users only**. Real projects should load secrets via environment variables or CI/CD secret management.

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

### Run the TestNG Suite

From the project root (where `pom.xml` and `testng.xml` are located), run:

```bash
mvn clean test
```

## 🏗 Project Layout

**Current structure (commit 2):**

- `src/main/java/com/suatc/qa/base` - Core base classes (`BasePage`)
- `src/main/java/com/suatc/qa/config` - Configuration (`ConfigReader`)
- `src/main/java/com/suatc/qa/factory` - Driver management (`DriverFactory`)
- `src/main/java/com/suatc/qa/pages` - Page objects (`LoginPage`, `InventoryPage`)
- `src/main/resources` - Framework configuration (`config.properties`, `log4j2.xml`)
- `src/test/java/com/suatc/qa/base` - Test base classes (`BaseTest`)
- `src/test/java/com/suatc/qa/hooks` - Cucumber hooks (`Hooks`)
- `src/test/java/com/suatc/qa/tests` - UI TestNG tests (e.g. `LoginTest`)
- `src/test/resources` - Test resources (e.g. feature files – planned)
- `testng.xml` - TestNG suite entry point
- `pom.xml` - Maven configuration and dependencies

**Planned modules:**

- `src/main/java/com/suatc/qa/utils` - Shared utilities
- `src/main/java/com/suatc/qa/api` - API clients and request builders
- `src/test/java/com/suatc/qa/stepdefinitions` - Cucumber step definitions
- `src/test/java/com/suatc/qa/runners` - Cucumber + TestNG runners
