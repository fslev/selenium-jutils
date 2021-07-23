package io.selenium.utils;

import io.selenium.utils.handler.ContextLocatingElementHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ElementContextLocator extends Retry implements ElementLocator {

    protected final SearchContext searchContext;
    protected final boolean shouldCache;
    protected WebElement cachedElement;
    protected List<WebElement> cachedElementList;
    protected By by;

    public ElementContextLocator(SearchContext searchContext, Field field, Duration duration, List<Class<? extends Throwable>> troubles) {
        this(searchContext, new Annotations(field), duration, troubles);
    }

    public ElementContextLocator(SearchContext searchContext, AbstractAnnotations annotations, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(duration, troubles);
        this.searchContext = searchContext;
        this.shouldCache = annotations.isLookupCached();
        this.by = annotations.buildBy();
    }

    @Override
    public WebElement findElement() {
        if (cachedElement != null && shouldCache()) {
            return cachedElement;
        }

        WebElement element = searchContext.findElement(by);

        if (shouldCache()) {
            cachedElement = element;
        }

        return element;
    }

    @Override
    public List<WebElement> findElements() {
        if (cachedElementList != null && shouldCache()) {
            return cachedElementList;
        }

        List<WebElement> elements = searchContext.findElements(by);

        if (shouldCache()) {
            cachedElementList = elements;
        }

        return elements;
    }

    public List<WebElement> findProxyElements() {

        List<WebElement> elements = findElements();
        List<WebElement> proxyElements = new ArrayList<>();
        for (int i = 0; i < elements.size(); i++) {
            proxyElements.add((WebElement) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                    new Class[]{WebElement.class, WrapsElement.class, Locatable.class},
                    new ContextLocatingElementHandler(this, i, duration, troubles)));
        }

        if (shouldCache()) {
            cachedElementList = elements;
        }

        return proxyElements;
    }

    protected boolean shouldCache() {
        return shouldCache;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " '" + by + "'";
    }
}
