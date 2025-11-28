package com.suatc.qa.utils;

import com.suatc.qa.factory.DriverFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {
    }

    public static String takeScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();

        if (!(driver instanceof TakesScreenshot)) {
            return null;
        }

        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String fileName = testName + "_" + timestamp + ".png";

        Path screenshotsDir = Paths.get("target", "screenshots");
        try {
            Files.createDirectories(screenshotsDir);
            Path destination = screenshotsDir.resolve(fileName);
            Files.copy(screenshot.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (IOException e) {
            // In a listener, we *never* want to break the test run because of screenshot failure
            return null;
        }
    }
}
