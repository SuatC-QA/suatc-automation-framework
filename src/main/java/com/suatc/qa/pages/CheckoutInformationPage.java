package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;

public class CheckoutInformationPage extends BasePage {

    private static final By FIRST_NAME_INPUT = By.id("first-name");
    private static final By LAST_NAME_INPUT = By.id("last-name");
    private static final By POSTAL_CODE_INPUT = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");

    public CheckoutInformationPage enterCustomerInformation(String firstName, String lastName, String postalCode) {
        logger.info("Entering customer information: {} {}, {}", firstName, lastName, postalCode);
        waitAndType(FIRST_NAME_INPUT, firstName);
        waitAndType(LAST_NAME_INPUT, lastName);
        waitAndType(POSTAL_CODE_INPUT, postalCode);
        return this;
    }

    public CheckoutOverviewPage continueToOverview() {
        logger.info("Continuing to checkout overview page");
        waitAndClick(CONTINUE_BUTTON);
        return new CheckoutOverviewPage();
    }
}
