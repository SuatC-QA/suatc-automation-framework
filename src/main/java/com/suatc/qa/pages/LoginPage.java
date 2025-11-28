package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {

    private static final By USERNAME_FIELD = By.id("user-name");
    private static final By PASSWORD_FIELD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");

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
        waitAndType(PASSWORD_FIELD, password);
    }

    private void typeUsername(String username) {
        waitAndType(USERNAME_FIELD, username);
    }

}
