package com.suatc.qa.tests.ui;

import com.suatc.qa.base.BaseTest;
import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.pages.InventoryPage;
import com.suatc.qa.pages.LoginPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTest extends BaseTest {

    @Test
    public void validUserCanLogin() {
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
}
