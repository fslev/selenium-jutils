package io.selenium.utils;

import io.selenium.utils.handler.ContextLocatingElementHandler;
import io.selenium.utils.handler.ContextLocatingElementListHandler;
import io.selenium.utils.handler.InitWebContextWithRetriesHandler;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.*;
import java.util.List;

public class FieldContextDecorator implements FieldDecorator {

    protected ElementContextLocatorFactory factory;

    public FieldContextDecorator(ElementContextLocatorFactory factory) {
        this.factory = factory;
    }

    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (!(WebElement.class.isAssignableFrom(field.getType()) || WebContext.class.isAssignableFrom(field.getType())
                || isDecoratableList(field, WebElement.class) || isDecoratableList(field, WebContext.class))) {
            return null;
        }

        ElementContextLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        if (WebElement.class.isAssignableFrom(field.getType())) {
            return proxyForElementLocator(loader, locator);
        } else if (isDecoratableList(field, WebElement.class)) {
            return proxyForElementListLocator(loader, locator);
        } else if (WebContext.class.isAssignableFrom(field.getType())) {
            return initWebContext(loader, field.getType(), locator);
        } else {
            return null;
        }
    }

    protected boolean isDecoratableList(Field field, Class<?> clazz) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }

        Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

        if (!clazz.equals(listType)) {
            return false;
        }

        return field.getAnnotation(FindBy.class) != null ||
                field.getAnnotation(FindBys.class) != null ||
                field.getAnnotation(FindAll.class) != null;
    }

    protected WebElement proxyForElementLocator(ClassLoader loader, ElementContextLocator locator) {
        InvocationHandler handler = new ContextLocatingElementHandler(locator, factory.getDuration(), factory.getTroubles());
        return (WebElement) Proxy.newProxyInstance(loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler);
    }

    @SuppressWarnings("unchecked")
    protected List<WebElement> proxyForElementListLocator(ClassLoader loader, ElementContextLocator locator) {
        InvocationHandler handler = new ContextLocatingElementListHandler(locator, factory.getDuration(), factory.getTroubles());
        return (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }

    protected WebContext initWebContext(ClassLoader loader, Class<?> fieldClass, ElementLocator locator) {
        try {
            WebContext context = (WebContext) fieldClass.getConstructor().newInstance();
            InitWebContextWithRetriesHandler handler = new InitWebContextWithRetriesHandler(locator, factory.getTroubles(), factory.getDuration());
            return context;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
