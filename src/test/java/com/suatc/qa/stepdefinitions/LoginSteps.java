package com.suatc.qa.stepdefinitions;

import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.pages.InventoryPage;
import com.suatc.qa.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginSteps {

    private final ConfigReader config = ConfigReader.getInstance();

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage = new LoginPage().open();
    }

    @When("I log in as a standard user")
    public void iLogInAsAStandardUser() {
        String username = config.getProperty("user.standard");
        String password = config.getProperty("pass.standard");
        inventoryPage = loginPage.login(username, password);
    }

    @Then("I should see the products page")
    public void iShouldSeeTheProductsPage() {
        String title = inventoryPage.getProductsTitleText();
        assertThat(title)
                .as("Inventory page title")
                .isEqualTo("Products");
    }
}
