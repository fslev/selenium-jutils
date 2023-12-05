package io.selenium.util;

import io.selenium.util.pages.happtpath.GroceryListTab;
import io.selenium.util.pages.happtpath.GroceryPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HappyPathTest {

    private static final String SELENIUM_HUB_URL = "http://localhost:4445";
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
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        GroceryListTab groceryListTab = groceryPage.getGroceryListTab();
        List<GroceryListTab.Item> list = groceryListTab.getItems();
        assertEquals(6, list.size());
        assertEquals("Baking item1", list.get(0).getName().getText());
        assertEquals("Baking item2", list.get(1).getName().getText());
        assertEquals("Baking item3", list.get(2).getName().getText());
        assertEquals("Baking item4", list.get(3).getName().getText());
        assertEquals("Baking item5", list.get(4).getName().getText());
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
        } catch (IndexOutOfBoundsException ignored) {
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

    @Test
    public void checkGroceryListItems_whileDomIsConstantlyRefreshed() {
        GroceryPage groceryPage = new GroceryPage(driver);
        driver.get(GROCERY_APP_URL);
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        GroceryListTab groceryListTab = groceryPage.getGroceryListTab();
        driver.get(GROCERY_APP_URL);
        List<GroceryListTab.Item> list = groceryListTab.getItems();
        driver.get(GROCERY_APP_URL);
        assertEquals(6, list.size());
        driver.get(GROCERY_APP_URL);
        assertEquals("Baking item1", list.get(0).getName().getText());
        driver.get(GROCERY_APP_URL);
        assertEquals("Baking item2", list.get(1).getName().getText());
        driver.get(GROCERY_APP_URL);
        assertEquals("Baking item3", list.get(2).getName().getText());
        assertEquals("Baking item4", list.get(3).getName().getText());
        assertEquals("Baking item5", list.get(4).getName().getText());
        driver.get(GROCERY_APP_URL);
        assertEquals("Baking item6", list.get(5).getName().getText());
        // Delete items
        GroceryListTab.Item firstItem = list.get(0);
        driver.get(GROCERY_APP_URL);
        GroceryListTab.Item lastItem = list.get(5);
        driver.get(GROCERY_APP_URL);
        GroceryListTab.Item item3 = list.get(2);
        driver.get(GROCERY_APP_URL);
        assertEquals("Baking item3", item3.getName().getText());
        driver.get(GROCERY_APP_URL);
        firstItem.getRemoveButton().click();
        driver.get(GROCERY_APP_URL);
        groceryListTab.getItemInput().addItem("Custom baking item1");
        driver.get(GROCERY_APP_URL);
        assertEquals(6, list.size());
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

    @AfterEach
    public void cleanup() {
        if (driver != null) {
            driver.quit();
        }
    }
}
