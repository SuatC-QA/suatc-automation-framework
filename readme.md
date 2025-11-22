# SUATC Automation Framework

## 🚀 Overview

A hybrid UI + API test automation framework designed for scalability and maintainability.

Built with **Java 17**, **Selenium 4**, **TestNG**, and **Cucumber**, the framework follows the **Page Object Model (POM)** pattern to keep page structure and test logic cleanly separated.

The goal of this project is to demonstrate production-style framework design suitable for real-world SDET roles.

---

## 🛠 Tech Stack

- **Language:** Java 17
- **UI Automation:** Selenium 4.27.0
- **API Automation:** REST Assured 5.5.6, Jackson Databind 2.20.1
- **Test Frameworks:** TestNG 7.10.2, Cucumber 7.20.1 (TestNG runner)
- **Build Tool:** Maven
- **Logging:** Log4j2 (console + rolling file logs under `target/logs`)
- **Reporting:**
    - TestNG console output
    - Cucumber HTML reports via `maven-cucumber-reporting` (for Cucumber suites)
    - Allure integration planned

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
- `timeout.implicit` - implicit wait (set to `0` when using explicit waits)
- `timeout.default` - default explicit wait timeout (seconds)
- `timeout.page_load` - page load timeout (seconds)
- `screenshot.on_failure` - `true` / `false` flag for screenshot behavior on test failure
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

_Current (initial) structure:_

- `src/main/resources` - core framework configuration (`log4j2.xml`, `config.properties`)
- `src/test/resources` - feature files (planned) and other test-only resources
- `testng.xml` - main TestNG suite entry point
- `pom.xml` - Maven project + dependencies

_Planned/expanding package structure (Core modules such as driver factory, utils, and base classes will be added as the framework grows)

- `src/main/java/com/suatc/qa/base` - Base Setup
- `src/main/java/com/suatc/qa/factory` - Driver Management
- `src/main/java/com/suatc/qa/utils` - Utilities
- `src/main/java/com/suatc/qa/config` - ConfigReader, environment helpers
- `src/main/java/com/suatc/qa/pages` - Page Objects
- `src/main/java/com/suatc/qa/api` - API clients
- `src/test/java/com/suatc/qa/tests` - UI/API TestNG tests
- `src/test/java/com/suatc/qa/stepdefinitions` - Cucumber step definitions
- `src/test/java/com/suatc/qa/runners` - TestNG+Cucumber runners
