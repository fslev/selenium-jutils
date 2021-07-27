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
Decorate your Page Object Model class instance using the FieldContextDecorator.class:  
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
You can group WebElements under the same class which extends WebContext and each element will be located relative to the search context of the class.  
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
See how easy is to retrieve all grocery list items:
```java
GroceryPage groceryPage = new GroceryPage(driver);
List<Item> itemList = groceryPage.getGroceryListTab().getItems();

assertEquals(6, itemList.size());
assertEquals("Baking item1", itemList.get(0).getName().getText());
// Remove 4th item
list.get(3).getRemoveButton().click();
assertEquals(5, itemList.size());
```

