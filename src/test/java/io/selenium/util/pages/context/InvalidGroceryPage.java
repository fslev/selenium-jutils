package io.selenium.util.pages.context;

import io.selenium.util.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class InvalidGroceryPage extends BasePage {


    @FindBy(xpath = "//app-departments")
    private InvalidDepartmentsTab invalidDepartmentsTab;

    public InvalidGroceryPage(WebDriver driver) {
        super(driver);
    }
}
