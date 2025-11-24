package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;

import org.openqa.selenium.By;

public class InventoryPage extends BasePage {

    private static final By PRODUCTS_TITLE = By.className("title");

    public String getProductsTitleText() {
        return waitForVisible(PRODUCTS_TITLE).getText();
    }
}
