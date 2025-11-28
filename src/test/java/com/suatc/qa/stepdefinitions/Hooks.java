package com.suatc.qa.stepdefinitions;

import com.suatc.qa.factory.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp() {
        logger.info("Initializing WebDriver for Cucumber scenario");
        DriverFactory.initDriver();
    }

    @After
    public void tearDown() {
        logger.info("Quitting WebDriver for Cucumber scenario");
        DriverFactory.quitDriver();
    }
}
