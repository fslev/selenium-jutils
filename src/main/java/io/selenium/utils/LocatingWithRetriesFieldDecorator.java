package io.selenium.utils;

import io.selenium.utils.handler.ElementInvocationWithRetriesHandler;
import io.selenium.utils.handler.ElementListInvocationWithRetriesHandler;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class LocatingWithRetriesFieldDecorator extends DefaultFieldDecorator {

    protected final Duration duration;
    protected final Set<Class<? extends Throwable>> troubles;

    public LocatingWithRetriesFieldDecorator(WebDriver driver, Duration duration, Set<Class<? extends Throwable>> troubles) {
        super(new DefaultElementLocatorFactory(driver));
        this.duration = duration;
        this.troubles = troubles;
    }

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new ElementInvocationWithRetriesHandler(locator, duration, troubles);
        return (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new ElementListInvocationWithRetriesHandler(locator, duration, troubles);
        return (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }
}
