package com.suatc.qa.factory;

import com.suatc.qa.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized WebDriver management with ThreadLocal support for parallel execution.
 */
public final class DriverFactory {

    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        if (driver.get() != null) {
            logger.debug("WebDriver already initialized for current thread");
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

        logger.info("Initializing WebDriver: browser={}, headless={}", browser, headless);

        WebDriver webDriver = switch (browser) {
            case "chrome" -> createChromeDriver(headless);
            case "firefox" -> createFirefoxDriver(headless);
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser + ". Supported: chrome, firefox");
        };

        long implicitTimeout = Long.parseLong(config.getProperty("timeout.implicit"));
        long pageLoadTimeout = Long.parseLong(config.getProperty("timeout.page_load"));

        logger.debug("Configuring timeouts: implicit={}s, pageLoad={}s",
                implicitTimeout, pageLoadTimeout);

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

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection");

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
            logger.info("Quitting WebDriver for current thread");
            try {
                webDriver.quit();
            } finally {
                driver.remove();
            }
        } else {
            logger.debug("No WebDriver to quit for current thread");
        }
    }
}
