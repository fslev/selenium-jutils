package io.selenium.util.pages.context;

import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GroceryListTab extends WebContext {

    @FindBy(xpath = ".//li")
    private List<Item> items;

    @FindBy(xpath = ".//div[@_ngcontent-c1=''][2]")
    private ItemInput itemInput;

    public List<Item> getItems() {
        return items;
    }

    public ItemInput getItemInput() {
        return itemInput;
    }

    public static class Item extends WebContext {
        @FindBy(css = "span")
        private WebElement name;
        @FindBy(xpath = ".//button[text()='Remove']")
        private WebElement removeButton;
        @FindBy(xpath = ".//button[text()='Toggle']")
        private WebElement toggleButton;
        @FindBy(xpath = ".//app-item")
        private WebElement itemWebElement;

        public WebElement getName() {
            return name;
        }

        public WebElement getRemoveButton() {
            return removeButton;
        }

        public WebElement getToggleButton() {
            return toggleButton;
        }

        public WebElement getItemWebElement() {
            return itemWebElement;
        }
    }

    public static class ItemInput extends WebContext {

        @FindBy(xpath = ".//input")
        private WebElement inputBox;
        @FindBy(xpath = ".//button")
        private WebElement addButton;

        public void addItem(String name) {
            inputBox.sendKeys(name);
            addButton.click();
        }
    }
}
