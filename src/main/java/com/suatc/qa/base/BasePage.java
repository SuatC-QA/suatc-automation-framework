package com.suatc.qa.base;

import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.exceptions.UiElementTimeoutException;
import com.suatc.qa.factory.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final ConfigReader config;
    private final String baseUrl;
    private final long explicitTimeout;

    protected BasePage() {
        this.driver = DriverFactory.getDriver();
        this.config = ConfigReader.getInstance();

        String env = config.getProperty("env", "qa")
                .toLowerCase()
                .trim();

        this.baseUrl = config.getProperty("url." + env);

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException(
                    "Config property 'url." + env + "' must be non-blank for env=" + env
            );
        }

        String timeoutProperty = config.getProperty("timeout.default");
        try {
            this.explicitTimeout = Long.parseLong(timeoutProperty);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "Config property 'timeout.default' must be a valid number of seconds, but was: " + timeoutProperty,
                    e
            );
        }

        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(this.explicitTimeout));
    }

    protected void openBaseUrl() {
        logger.info("Navigating to base URL: {}", baseUrl);

        try {
            driver.get(baseUrl);
        } catch (TimeoutException e) {
            logger.warn("Navigation to {} timed out once (likely headless flakiness). Retrying...", baseUrl, e);
            driver.get(baseUrl);
        }
    }

    protected void goTo(String path) {
        String url = resolveUrl(path);
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    private String resolveUrl(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            throw new IllegalArgumentException("Path must not be null or blank");
        }

        String path = rawPath.trim();

        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }

        String normalizedBase = baseUrl.endsWith("/")
                ? baseUrl.substring(0, baseUrl.length() - 1)
                : baseUrl;

        String normalizedPath = path.startsWith("/") ? path : "/" + path;

        return normalizedBase + normalizedPath;
    }

    protected WebElement waitForVisible(By locator) {
        return waitFor(
                visibilityOfElementLocated(locator),
                "visibility of element: " + locator
        );
    }

    protected WebElement waitForClickable(By locator) {
        return waitFor(
                elementToBeClickable(locator),
                "clickable element: " + locator
        );
    }

    protected void waitAndClick(By locator) {
        WebElement element = waitForClickable(locator);
        element.click();
    }

    protected void waitAndType(By locator, String text) {
        logger.debug("Typing into element {}: '{}'", locator, text);
        WebElement element = waitFor(
                visibilityOfElementLocated(locator),
                "visibility of element for typing: " + locator
        );
        element.clear();
        element.sendKeys(text);
    }

    protected <T> T waitFor(ExpectedCondition<T> condition, String description) {
        try {
            logger.debug("Waiting up to {}s for {}", explicitTimeout, description);
            return wait.until(condition);
        } catch (TimeoutException e) {
            String pageName = getClass().getSimpleName();
            logger.error("Timeout after {}s waiting for {} on page {}", explicitTimeout, description, pageName);
            throw new UiElementTimeoutException(
                    "Timed out after " + explicitTimeout +
                            "s waiting for " + description +
                            " on page: " + pageName,
                    e
            );
        }
    }
}
