package io.selenium.util;

import io.selenium.util.pages.cache.GroceryListTab;
import io.selenium.util.pages.cache.GroceryPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CachedLookupTest {

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
    public void checkCachedGroceryListItems() {
        GroceryPage groceryPage = new GroceryPage(driver);
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        List<GroceryListTab.Item> list = groceryPage.getGroceryListTab().getItems();
        assertEquals(6, list.size());
        assertEquals("Baking item1", list.get(0).getName().getText());
        assertEquals("Baking item2", list.get(1).getName().getText());
        assertEquals("Baking item3", list.get(2).getName().getText());
        assertEquals("Baking item4", list.get(3).getName().getText());
        assertEquals("Baking item5", list.get(4).getName().getText());
        assertEquals("Baking item6", list.get(5).getName().getText());

        GroceryListTab.Item firstItem = groceryPage.getGroceryListTab().getItems().get(0);
        GroceryListTab.Item lastItem = groceryPage.getGroceryListTab().getItems().get(5);
        GroceryListTab.Item item2 = groceryPage.getGroceryListTab().getItems().get(1);

        firstItem.getRemoveButton().click();
        lastItem.getRemoveButton().click();
        assertEquals("Baking item2", item2.getName().getText());
        assertThrows(StaleElementReferenceException.class, () ->
                firstItem.getName().getText(), "Should throw stale element reference exception");
        assertThrows(StaleElementReferenceException.class, () ->
                lastItem.getName().getText(), "Should throw stale element reference exception");
    }

    @Test
    public void checkGroceryListItemsAreCached() {
        GroceryPage groceryPage = new GroceryPage(driver);
        GroceryListTab.Item item1 = groceryPage.getGroceryListTab().getItems().get(0);
        GroceryListTab.Item item2 = groceryPage.getGroceryListTab().getItems().get(0);
        assertEquals(item1, item2);
    }

    @AfterEach
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
