package io.selenium.util;

import io.selenium.util.pages.GroceryPage;
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

public class IntegrationTest {

    private static final String SELENIUM_HUB_URL = "http://localhost:4444";
    private static final String GROCERY_APP_URL = "http://grocery-list:80";

    private WebDriver driver;

    @Before
    public void init() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL(SELENIUM_HUB_URL), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(45));
        driver.get(GROCERY_APP_URL);
    }

    @Test
    public void addGroceryListItem() {
        GroceryPage groceryPage = new GroceryPage(driver);
        assertEquals("Grocery List", groceryPage.getPageTitle().getText());
        System.out.println("1");
        List<WebElement> list=groceryPage.getItemList();
        System.out.println("2");
        list.size();
        System.out.println("3");
//        WebElement element=list.get(0);
        System.out.println("3a");
//        System.out.println(element.getText());
        System.out.println("4");
        System.out.println(list.size());
        System.out.println(list.get(5).getText());
//        String text = groceryPage.getGroceryListContext().getListElementContext().getItem().getText();
//        System.out.println(text);
    }

    @After
    public void cleanup() {
        if (driver != null) {
            driver.close();
        }
    }
}
