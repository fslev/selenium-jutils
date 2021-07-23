package io.selenium.utils;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ElementContextLocatorFactory extends Retry implements ElementLocatorFactory {

    protected final SearchContext searchContext;

    public ElementContextLocatorFactory(SearchContext searchContext) {
        this(searchContext, Duration.ofSeconds(0), new ArrayList<>());
    }

    public ElementContextLocatorFactory(SearchContext searchContext, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(duration, troubles);
        this.searchContext = searchContext;
    }

    @Override
    public ElementContextLocator createLocator(Field field) {
        return new ElementContextLocator(searchContext, field, duration, troubles);
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }
}
