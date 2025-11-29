package com.suatc.qa.tests.ui;

import com.suatc.qa.base.BaseTest;
import com.suatc.qa.config.ConfigReader;
import com.suatc.qa.pages.*;
import org.testng.annotations.Test;

import static com.suatc.qa.pages.InventoryPage.PRODUCT_SAUCE_LABS_BACKPACK;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTest extends BaseTest {

    private final ConfigReader config = ConfigReader.getInstance();
    private final String standardUsername = config.getProperty("user.standard");
    private final String standardPassword = config.getProperty("pass.standard");
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";
    private static final String POSTAL_CODE = "90001";

    @Test
    public void shouldCompleteCheckoutFlowForSingleItem() {
        LoginPage loginPage = new LoginPage();
        InventoryPage inventoryPage = loginPage
                .open()
                .login(standardUsername, standardPassword);

        CartPage cartPage = inventoryPage
                .addProductToCart(PRODUCT_SAUCE_LABS_BACKPACK)
                .openCart();

        CheckoutInformationPage checkoutInformationPage = cartPage.proceedToCheckout();

        CheckoutOverviewPage overviewPage = checkoutInformationPage
                .enterCustomerInformation(FIRST_NAME, LAST_NAME, POSTAL_CODE)
                .continueToOverview();

        assertThat(overviewPage.isItemPresent(PRODUCT_SAUCE_LABS_BACKPACK)).isTrue();
    }

}
