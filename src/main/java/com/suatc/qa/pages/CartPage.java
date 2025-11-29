package com.suatc.qa.pages;

import com.suatc.qa.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private static final By CART_ITEM_NAMES = By.className("inventory_item_name");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public boolean isItemInCart(String productName) {
        logger.info("Checking if product '{}' is present in the cart", productName);
        List<WebElement> items = driver.findElements(CART_ITEM_NAMES);
        return items.stream()
                .map(e -> e.getText().trim())
                .anyMatch(name -> name.equals(productName));
    }

    public CheckoutInformationPage proceedToCheckout() {
        logger.info("Proceeding to checkout from cart");
        waitAndClick(CHECKOUT_BUTTON);
        return new CheckoutInformationPage();
    }
}
