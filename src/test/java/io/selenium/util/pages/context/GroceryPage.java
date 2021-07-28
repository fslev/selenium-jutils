package io.selenium.util.pages.context;

import io.selenium.util.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class GroceryPage extends BasePage {

    @FindBy(xpath = "//app-root/a")
    private List<WebElement> tabs;

    @FindBy(xpath = "//app-departments")
    private DepartmentsTab departmentsTab;

    @FindBy(xpath = "//app-list")
    private GroceryListTab groceryListTab;

    public GroceryPage(WebDriver driver) {
        super(driver);
    }

    public DepartmentsTab getDepartmentsTab() {
        tabs.get(1).click();
        return departmentsTab;
    }

    public GroceryListTab getGroceryListTab() {
        tabs.get(0).click();
        return groceryListTab;
    }
}
