package io.selenium.util.pages;

import io.selenium.utils.WebContext;
import org.openqa.selenium.support.FindBy;

public class GroceryListContext extends WebContext {

    @FindBy(xpath = "//ul")
    private ListElementContext listElementContext;

    public ListElementContext getListElementContext() {
        return listElementContext;
    }
}
