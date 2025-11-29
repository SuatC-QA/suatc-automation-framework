package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;

public class InventoryPage extends BasePage {

    private static final By PRODUCTS_TITLE = By.className("title");
    private static final String ADD_TO_CART_BUTTON_BY_NAME_XPATH =
            "//*[@data-test='inventory-item-name' and normalize-space()='%s']" +
                    "/ancestor::*[@data-test='inventory-item']" +
                    "//button[contains(@data-test,'add-to-cart')]";
    public static final String PRODUCT_SAUCE_LABS_BACKPACK = "Sauce Labs Backpack";
    private static final By CART_ICON = By.id("shopping_cart_container");

    public String getProductsTitleText() {
        return waitForVisible(PRODUCTS_TITLE).getText();
    }

    public InventoryPage addProductToCart(String productName) {
        By addToCartButton = By.xpath(String.format(ADD_TO_CART_BUTTON_BY_NAME_XPATH, productName));
        logger.info("Adding product '{}' to cart", productName);
        waitAndClick(addToCartButton);
        return this;
    }

    public CartPage openCart() {
        logger.info("Opening cart");
        waitAndClick(CART_ICON);
        return new CartPage();
    }
}
