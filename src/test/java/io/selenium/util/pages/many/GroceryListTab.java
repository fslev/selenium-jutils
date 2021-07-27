package io.selenium.util.pages.many;

import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GroceryListTab extends WebContext {

    @FindAll({@FindBy(xpath = "//invalid"), @FindBy(xpath = ".//li//app-item")})
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
