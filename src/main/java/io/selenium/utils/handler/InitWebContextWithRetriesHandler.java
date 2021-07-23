package io.selenium.utils.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.time.Duration;
import java.util.List;

public class InitWebContextWithRetriesHandler {

    protected static final Logger LOG = LogManager.getLogger();

    protected final ElementLocator locator;
    protected final List<Class<? extends Throwable>> troubles;
    protected final Duration duration;

    public InitWebContextWithRetriesHandler(ElementLocator locator, List<Class<? extends Throwable>> troubles, Duration duration) {
        this.locator = locator;
        this.troubles = troubles;
        this.duration = duration;
    }

    public WebElement getElement() {
        return locator.findElement();
    }
}
