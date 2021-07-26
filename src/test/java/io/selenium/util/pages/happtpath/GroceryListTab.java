package io.selenium.util.pages.happtpath;

import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GroceryListTab extends WebContext {

    @FindBy(xpath = ".//li//app-item")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public static class Item extends WebContext {
        @FindBy(css = "span")
        private WebElement text;
        @FindBy(xpath = ".//button[text()='Remove']")
        private WebElement removeButton;
        @FindBy(xpath = ".//button[text()='Toggle']")
        private WebElement toggleButton;

        public WebElement getName() {
            return text;
        }

        public WebElement getRemoveButton() {
            return removeButton;
        }

        public WebElement getToggleButton() {
            return toggleButton;
        }
    }
}
