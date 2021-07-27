package io.selenium.util.pages.many;

import io.selenium.util.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

public class GroceryPage extends BasePage {

    @FindBy(xpath = "//h1")
    private WebElement pageTitle;

    @FindBys({@FindBy(xpath = "//app-list"), @FindBy(xpath = "//app-list[@_nghost-c1='']")})
    private GroceryListTab groceryListTab;

    @FindAll({@FindBy(xpath = "//invalid"), @FindBy(xpath = "//app-departments")})
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
