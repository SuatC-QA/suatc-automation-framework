package com.suatc.qa.base;

import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.exceptions.UiElementTimeoutException;
import com.suatc.qa.factory.DriverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private final String baseUrl;
    private final long explicitTimeout;
    protected final ConfigReader config;
    protected BasePage() {
        this.driver = DriverFactory.getDriver();

        this.config = ConfigReader.getInstance();
        this.baseUrl = config.getProperty("url.qa");
        this.explicitTimeout = Long.parseLong(
                config.getProperty("timeout.default")
        );


        this.wait = new WebDriverWait(this.driver, Duration.ofSeconds(this.explicitTimeout));
    }

    protected void openBaseUrl() {
        driver.get(baseUrl);
    }

    protected void goTo(String path) {
        driver.get(resolveUrl(path));
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
        WebElement element = waitFor(
                visibilityOfElementLocated(locator),
                "visibility of element for typing: " + locator
        );
        element.clear();
        element.sendKeys(text);
    }

    protected <T> T waitFor(ExpectedCondition<T> condition, String description) {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            String pageName = getClass().getSimpleName();
            throw new UiElementTimeoutException(
                    "Timed out after " + explicitTimeout +
                            "s waiting for " + description +
                            " on page: " + pageName,
                    e
            );
        }
    }
}
