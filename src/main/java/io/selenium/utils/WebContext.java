package io.selenium.utils;

import org.openqa.selenium.SearchContext;

public abstract class WebContext {
    private SearchContext searchContext;

    public void setSearchContext(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public SearchContext getSearchContext() {
        return searchContext;
    }
}
