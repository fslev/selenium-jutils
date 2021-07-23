package io.selenium.utils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

public class WebContext implements WrapsElement {

    private WebElement wrappedElement;

    public void setWrappedElement(WebElement element) {
        this.wrappedElement = element;
    }

    public WebElement getWrappedElement() {
        return wrappedElement;
    }
}
