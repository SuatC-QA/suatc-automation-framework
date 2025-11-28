package com.suatc.qa.tests.ui;

import com.suatc.qa.base.BaseTest;
import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.pages.InventoryPage;
import com.suatc.qa.pages.LoginPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test
    public void shouldLoginSuccessfullyWithValidUser() {
        ConfigReader config = ConfigReader.getInstance();
        String username = config.getProperty("user.standard");
        String password = config.getProperty("pass.standard");

        InventoryPage inventoryPage = new LoginPage()
                .open()
                .login(username, password);

        String productsTitleText = inventoryPage.getProductsTitleText();
        assertThat(productsTitleText)
                .as("Inventory page products title")
                .isEqualTo("Products");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return new Object[][]{
                // username, password, expectedError
                {"standard_user", "wrong_password",
                        "Epic sadface: Username and password do not match any user in this service"},
                {"locked_out_user", "secret_sauce",
                        "Epic sadface: Sorry, this user has been locked out."},
                {"", "secret_sauce",
                        "Epic sadface: Username is required"},
                {"standard_user", "",
                        "Epic sadface: Password is required"}
        };
    }

    @Test(dataProvider = "invalidLoginData")
    public void shouldShowErrorMessageForInvalidLogin(
            String username,
            String password,
            String expectedError
    ) {
        LoginPage loginPage = new LoginPage()
                .open()
                .attemptLogin(username, password);

        String actualError = loginPage.getErrorMessage();

        assertThat(actualError)
                .as("Error message for username='%s', password='%s'", username, password)
                .isEqualTo(expectedError);
    }

}
