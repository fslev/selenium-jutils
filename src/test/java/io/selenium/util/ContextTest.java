package io.selenium.util;

import io.selenium.util.pages.context.DepartmentsTab;
import io.selenium.util.pages.context.GroceryPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ContextTest {

    private static final String SELENIUM_HUB_URL = "http://localhost:4444";
    private static final String GROCERY_APP_URL = "http://grocery-list:80";

    private WebDriver driver;

    @Before
    public void init() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(GROCERY_APP_URL);
    }

    @Test
    public void checkGroceryListItems() {
        GroceryPage groceryPage = new GroceryPage(driver);
        DepartmentsTab departmentsTab = groceryPage.getDepartmentsTab();
        List<DepartmentsTab.Department> departmentsList = departmentsTab.getDepartments();
        assertEquals(6, departmentsList.size());
        DepartmentsTab.Department department = departmentsList.get(3);
        WebElement removeButton = department.getContents().get(0).getButton();
        assertEquals("Remove", removeButton.getText());
        removeButton.click();
        assertEquals(5, departmentsList.size());
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
