package io.selenium.util.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GroceryPage extends BasePage {

    @FindBy(xpath = "//h1")
    private WebElement pageTitle;

//    @FindBy(xpath = "//app-list")
//    private GroceryListContext groceryListContext;

    @FindBy(xpath = "//app-list//li")
    private List<WebElement> itemList;

    public GroceryPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getPageTitle() {
        return pageTitle;
    }

    public List<WebElement> getItemList() {
        return itemList;
    }

    //    public GroceryListContext getGroceryListContext() {
//        return groceryListContext;
//    }
}
