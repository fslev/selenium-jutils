package io.selenium.util;

import io.selenium.util.pages.happtpath.GroceryListTab;
import io.selenium.util.pages.happtpath.GroceryPage;
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

import static org.junit.Assert.*;

public class HappyPathTest {

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
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        List<GroceryListTab.Item> list = groceryPage.getGroceryListTab().getItems();
        assertEquals(6, list.size());
        assertEquals("Baking item1", list.get(0).getName().getText());
        assertEquals("Baking item2", list.get(1).getName().getText());
        assertEquals("Baking item3", list.get(2).getName().getText());
        assertEquals("Baking item4", list.get(3).getName().getText());
        assertEquals("Baking item5", list.get(4).getName().getText());
        assertEquals("Baking item6", list.get(5).getName().getText());
    }

    @Test
    public void checkGroceryListItemsAreNotCached() {
        GroceryPage groceryPage = new GroceryPage(driver);
        GroceryListTab.Item item1 = groceryPage.getGroceryListTab().getItems().get(0);
        GroceryListTab.Item item2 = groceryPage.getGroceryListTab().getItems().get(0);
        assertNotEquals(item1, item2);
    }

    @Test
    public void checkGroceryListItems_invalidContextLocator() {
        GroceryPage groceryPage = new GroceryPage(driver);
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        List<GroceryListTab.Item> list = groceryPage.getInvalidGroceryListTab().getItems();
        try {
            list.size();
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return;
        }
        fail("Invalid Grocery List Context should fail with NoSuchElementException");
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.close();
        }
    }
}
