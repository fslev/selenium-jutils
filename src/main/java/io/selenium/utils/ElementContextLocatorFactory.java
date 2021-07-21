package io.selenium.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ElementContextLocatorFactory implements ElementLocatorFactory {

    protected final SearchContext searchContext;
    protected final List<By> byChain;

    public ElementContextLocatorFactory(SearchContext searchContext) {
        this(searchContext, new ArrayList<>());
    }

    public ElementContextLocatorFactory(SearchContext searchContext, List<By> byChain) {
        this.searchContext = searchContext;
        this.byChain = byChain;
    }

    @Override
    public ElementLocator createLocator(Field field) {
        return new ElementContextLocator(searchContext, byChain, field);
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }

    public List<By> getByChain() {
        return byChain;
    }
}
