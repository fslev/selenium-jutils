package io.selenium.util.pages;

import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ListElementContext extends WebContext {

    @FindBy(xpath = "//li")
    private WebElement item;

    public WebElement getItem() {
        return item;
    }
}
