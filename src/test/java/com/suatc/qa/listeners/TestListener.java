package com.suatc.qa.listeners;

import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        ConfigReader config = ConfigReader.getInstance();

        boolean screenshotOnFailure = Boolean.parseBoolean(
                config.getPropertyOrDefault("screenshot.on_failure", "true")
        );

        if (!screenshotOnFailure) {
            logger.info("Skipping screenshot for {} because screenshot.on_failure=false",
                    result.getMethod().getMethodName());
            return;
        }

        String testName = result.getTestClass().getName() + "." + result.getMethod().getMethodName();

        String path = ScreenshotUtils.takeScreenshot(testName);

        if (path != null) {
            logger.info("Saved failure screenshot for {} at {}", testName, path);
        } else {
            logger.warn("Failed to capture screenshot for {}", testName);
        }
    }
}
