# Selenium JUtils
[![Maven Central](https://img.shields.io/maven-central/v/io.github.fslev/selenium-jutils.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.fslev%22%20AND%20a:%22selenium-jutils%22)
![Build status](https://github.com/fslev/selenium-jutils/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)
[![Coverage Status](https://coveralls.io/repos/github/fslev/selenium-jutils/badge.svg?branch=main)](https://coveralls.io/github/fslev/selenium-jutils?branch=main)

A light Java library designed to tackle the shortcomings of Selenium Page Object Model and Page Factory features.  
It provides the following enhancements:
- __WebContext__, used for locating web elements relative to a parent element  
- __Retry on error mechanism__, activated when an error or exception occurs while locating a web element, such as StaleElementReferenceException.  

### Maven Central
```
<dependency>
   <groupId>io.github.fslev</groupId>
   <artifactId>selenium-jutils</artifactId>
   <version>${latest.version}</version>
</dependency>

Gradle: compile("io.github.fslev:selenium-jutils:${latest.version}")
```  

### Required dependencies
Selenium-JUtils uses the _selenium-java_ library. Set it inside your project, in addition to _selenium-jutils_ dependency:  
```
<dependency>
   <groupId>org.seleniumhq.selenium</groupId>
   <artifactId>selenium-java</artifactId>
   <version>${selenium.java.version}</version>
</dependency>
```

# How to use
Decorate your Page Object class instance using PageFactory and FieldContextDecorator:  
```java
public class BasePage {
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new FieldContextDecorator(new ElementContextLocatorFactory(driver)), this);
    }
}
```

# Features

## WebContext
You can group WebElements or other context classes under the same class which extends WebContext and each element will be located relative to the search context of its wrapper class.  
__Example:__

```java
public class GroceryPage extends BasePage {

    @FindBy(xpath = "//app-list")
    private GroceryListTab groceryListTab;
}

public class GroceryListTab extends WebContext {

    @FindBy(xpath = ".//li//app-item")
    private List<Item> items;
}

public static class Item extends WebContext {
    
    @FindBy(css = "span")
    private WebElement name;
    @FindBy(xpath = ".//button[text()='Remove']")
    private WebElement removeButton;
} 
```
See how easy is to manage a grocery list:
```java
GroceryPage groceryPage = new GroceryPage(driver);
List<Item> itemList = groceryPage.getGroceryListTab().getItems();

assertEquals(6, itemList.size());
assertEquals("Baking item1", itemList.get(0).getName().getText());
// Remove 4th item
list.get(3).getRemoveButton().click();
assertEquals(5, itemList.size());
```
__Behind the scenes__
```java
new GroceryPage(driver).getGroceryListTab().getItems().get(2).getName()
```
==>
```java
driver.findElement(By.xpath("//app-list"))
        .findElements(By.xpath(".//li//app-item"))
          .get(2)
            .findElement(By.cssSelector("span"))
```
Behind the scenes, the web element(s) you get, are nothing but Java proxies which are first locating the corresponding element every time you interact with it.
It preserves the default behaviour and features of Selenium Page Factory: https://github.com/SeleniumHQ/selenium/wiki/PageFactory

## Retry on error
There is a possibility that after locating a web element(s) but before interacting with it, the DOM gets refreshed, in which case you will receive a _StaleElementReferenceException_.  
```java
new GroceryPage(driver).getGroceryListTab().getItems().get(2)
        // <-- DOM is refreshed
        .getName()
```
or
```java
driver.findElement(By.xpath("//app-list"))
        .findElements(By.xpath(".//li//app-item"))
          .get(2)
            // <-- DOM is refreshed 
            .findElement(By.cssSelector("span"))
```
=> _StaleElementReferenceException_  

Or maybe the element becomes not selectable for a very short period of time, in which case you get _ElementNotSelectableException_.

The __retry on error__ mechanism tackles these problems by trying to locate the web element again if any specific error occurs.
In order to activate it, instantiate the _ElementContextLocatorFactory_ with a specified List of errors or exceptions, upon which web element localisation and interaction should be retried, and a duration timeout. 
```java
PageFactory.initElements(new FieldContextDecorator(new ElementContextLocatorFactory(
                driver, Duration.ofSeconds(20), Collections.singletonList(StaleElementReferenceException.class, 
                    ElementNotSelectableException.class ))), this);

```

### Big picture
You can take a look over the project tests in order to get a clearer picture on how the WebContext and Retry on error mechanism work.  

## Other features
It comes with [**jtest-utils**](https://github.com/fslev/jtest-utils), which is a friendly library used for matching different types of objects, for making HTTP requests, SQL database queries, etc.  



