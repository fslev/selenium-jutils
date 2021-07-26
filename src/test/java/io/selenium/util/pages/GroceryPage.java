package io.selenium.util.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class GroceryPage extends BasePage {

    @FindBy(xpath = "//h1")
    private WebElement pageTitle;

    @FindBy(xpath = "//app-list")
    private GroceryListTab groceryListTab;

    @FindBy(xpath = "//app-departments")
    private DepartmentsTab departmentsTab;

    @FindBy(xpath = "//app-list-invalid")
    private GroceryListTab invalidGroceryTab;

    public GroceryPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getPageTitle() {
        return pageTitle;
    }

    public GroceryListTab getGroceryListTab() {
        return groceryListTab;
    }

    public DepartmentsTab getDepartmentsTab() {
        return departmentsTab;
    }

    public GroceryListTab getInvalidGroceryListTab() {
        return invalidGroceryTab;
    }
}
