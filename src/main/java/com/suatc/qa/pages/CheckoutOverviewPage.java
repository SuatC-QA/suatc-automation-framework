package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckoutOverviewPage extends BasePage {

    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By ITEM_NAMES = By.className("inventory_item_name");

    public String getPageTitle() {
        return waitForVisible(PAGE_TITLE).getText();
    }

    public boolean isItemPresent(String productName) {
        logger.info("Verifying product '{}' is present on checkout overview", productName);
        List<WebElement> items = driver.findElements(ITEM_NAMES);
        return items.stream()
                .map(e -> e.getText().trim())
                .anyMatch(name -> name.equals(productName));
    }
}
