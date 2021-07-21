package io.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ElementContextLocator implements ElementLocator {

    protected final SearchContext searchContext;
    protected final boolean shouldCache;
    protected final List<By> byChain = new ArrayList<>();
    protected WebElement cachedElement;
    protected List<WebElement> cachedElementList;

    public ElementContextLocator(SearchContext searchContext, List<By> byChain, Field field) {
        this(searchContext, byChain, new Annotations(field));
    }

    public ElementContextLocator(SearchContext searchContext, List<By> byChain, AbstractAnnotations annotations) {
        this.searchContext = searchContext;
        this.shouldCache = annotations.isLookupCached();
        this.byChain.addAll(byChain);
        this.byChain.add(annotations.buildBy());
    }

    @Override
    public WebElement findElement() {
        if (cachedElement != null && shouldCache()) {
            return cachedElement;
        }

        WebElement element = searchContext.findElement(byChain.get(0));
        if (byChain.size() > 1) {
            for (int i = 1; i < byChain.size(); i++) {
                element = element.findElement(byChain.get(i));
            }
        }

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

        List<WebElement> elements = searchContext.findElements(byChain.get(0));
        if (shouldCache()) {
            cachedElementList = elements;
        }

        return elements;
    }

    protected boolean shouldCache() {
        return shouldCache;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " '" + byChain + "'";
    }
}
