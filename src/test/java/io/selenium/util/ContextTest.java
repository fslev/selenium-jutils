package io.selenium.util;

import io.selenium.util.pages.context.DepartmentsTab;
import io.selenium.util.pages.context.GroceryListTab;
import io.selenium.util.pages.context.GroceryPage;
import io.selenium.util.pages.context.InvalidGroceryPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {

    private static final String SELENIUM_HUB_URL = "http://localhost:4444";
    private static final String GROCERY_APP_URL = "http://grocery-list:80";

    private WebDriver driver;

    @BeforeEach
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

        // Also check noContext fields are null
        assertNull(departmentsTab.getNoContext());
        assertNull(departmentsTab.getNoContexts());
    }

    @Test
    public void contextSearch() {
        GroceryPage groceryPage = new GroceryPage(driver);
        GroceryListTab groceryListTab = groceryPage.getGroceryListTab();
        List<GroceryListTab.Item> list = groceryListTab.getItems();
        GroceryListTab.Item item = list.get(2);
        assertEquals("Baking item3", item.getName().getText());
        // Get search context and find elements relative to it
        assertEquals("Baking item3", item.getSearchContext().findElement(By.cssSelector("span")).getText());
        assertEquals("Baking item3", item.getItemWebElement().findElement(By.xpath("span")).getText());

        // Get item web element and find elements relative to it
        item = list.get(3);
        assertEquals("Baking item4", item.getSearchContext().findElement(By.cssSelector("span")).getText());
        assertEquals("Baking item4", item.getItemWebElement().findElement(By.xpath("./span")).getText());
        assertEquals("Baking item4", item.getSearchContext().findElement(By.cssSelector("span")).getText());
        new FluentWait<>(groceryPage.getDriver()).withTimeout(Duration.ofSeconds(5)).until(ExpectedConditions.textToBePresentInElement(item.getSearchContext().findElement(By.cssSelector("span")), "Baking item4"));
    }


    @Test
    public void checkWebContextWithOneArgumentConstructor() {
        GroceryPage groceryPage = new GroceryPage(driver);
        List<DepartmentsTab.DepartmentInvalid> invalidDepartments = groceryPage.getDepartmentsTab().getInvaliDepartments();
        try {
            invalidDepartments.get(0).getContents().size();
            fail("Should fail. WebContext with one argument constructor");
        } catch (RuntimeException ignored) {
        }
        try {
            new InvalidGroceryPage(driver);
            fail("Should fail. WebContext with one argument constructor");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Cannot create instance from"));
        }
    }

    @AfterEach
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
