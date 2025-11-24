package com.suatc.qa.factory;

import com.suatc.qa.config.ConfigReader;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Centralized WebDriver management with ThreadLocal support for parallel execution.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
        // Utility class
    }

    public static void initDriver() {
        if (driver.get() != null) {
            return;
        }

        ConfigReader config = ConfigReader.getInstance();

        String browser = config
                .getPropertyOrDefault("browser", "chrome")
                .toLowerCase()
                .trim();

        boolean headless = Boolean.parseBoolean(
                config.getPropertyOrDefault("browser.headless", "false")
        );

        WebDriver webDriver = switch (browser) {
            case "chrome" -> createChromeDriver(headless);
            case "firefox" -> createFirefoxDriver(headless);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser + ". Supported: chrome, firefox");
        };

        long implicitTimeout = Long.parseLong(config.getProperty("timeout.implicit"));
        long pageLoadTimeout = Long.parseLong(config.getProperty("timeout.page_load"));

        webDriver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitTimeout))
                .pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

        if (headless) {
            webDriver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            webDriver.manage().window().maximize();
        }

        driver.set(webDriver);
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();

        if (headless) {
            options.addArguments("--headless=new", "--disable-gpu");
        }

        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();

        if (headless) {
            options.addArguments("-headless");
        }

        return new FirefoxDriver(options);
    }

    public static WebDriver getDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized. Call DriverFactory.initDriver() before using getDriver()."
            );
        }
        return webDriver;
    }

    public static void quitDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            try {
                webDriver.quit();
            } finally {
                driver.remove();
            }
        }
    }
}
