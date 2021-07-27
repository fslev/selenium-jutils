package io.selenium.util;

import io.selenium.util.pages.many.GroceryListTab;
import io.selenium.util.pages.many.GroceryPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FindByManyTest {

    private static final String SELENIUM_HUB_URL = "http://localhost:4444";
    private static final String GROCERY_APP_URL = "http://grocery-list:80";

    private WebDriver driver;

    @Before
    public void init() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get(GROCERY_APP_URL);
    }

    @Test
    public void checkGroceryListItems() {
        GroceryPage groceryPage = new GroceryPage(driver);
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        GroceryListTab groceryListTab = groceryPage.getGroceryListTab();
        List<GroceryListTab.Item> list = groceryListTab.getItems();
        assertEquals(6, list.size());
        assertEquals("Baking item1", list.get(0).getName().getText());
        assertEquals("Baking item6", list.get(5).getName().getText());
        // Delete items
        GroceryListTab.Item firstItem = list.get(0);
        GroceryListTab.Item lastItem = list.get(5);
        GroceryListTab.Item item3 = list.get(2);
        assertEquals("Baking item3", item3.getName().getText());
        firstItem.getRemoveButton().click();
        // First item should point to the updated list
        assertEquals("Baking item2", firstItem.getName().getText());
        // List now has 5 items
        try {
            lastItem.getRemoveButton().click();
            fail("Last Item shouldn't be accessible any more");
        } catch (IndexOutOfBoundsException e) {
        }
        // Refresh last item
        lastItem = list.get(4);
        lastItem.getRemoveButton().click();
        // List now has 4 items
        assertEquals(4, list.size());
        // Item3 will point to 3rd element from the updated list
        assertEquals("Baking item4", item3.getName().getText());

        // Add new item
        groceryListTab.getItemInput().addItem("Custom baking item1");
        // List now has 5 items
        assertEquals(5, list.size());
        // Last item now points to the last added item
        assertEquals("Custom baking item1", lastItem.getName().getText());
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
