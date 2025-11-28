package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.id("user-name");
    private static final By PASSWORD_INPUT = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    public LoginPage open() {
        openBaseUrl();
        return this;
    }

    public InventoryPage login(String username, String password) {
        logger.info("Logging in with user {}", username);
        typeUsername(username);
        typePassword(password);
        clickLogin();
        return new InventoryPage();
    }

    private void clickLogin() {
        waitAndClick(LOGIN_BUTTON);
    }

    private void typePassword(String password) {
        waitAndType(PASSWORD_INPUT, password);
    }

    private void typeUsername(String username) {
        waitAndType(USERNAME_INPUT, username);
    }

    /**
     * Attempts login and stays on the LoginPage, used for negative/error scenarios.
     */
    public LoginPage attemptLogin(String username, String password) {
        logger.info("Attempting login with username: {}", username);
        waitAndType(USERNAME_INPUT, username);
        waitAndType(PASSWORD_INPUT, password);
        waitAndClick(LOGIN_BUTTON);
        return this;
    }

    public String getErrorMessage() {
        return waitForVisible(ERROR_MESSAGE).getText();
    }

}
