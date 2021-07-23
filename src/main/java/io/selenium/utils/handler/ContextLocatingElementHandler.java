package io.selenium.utils.handler;

import io.selenium.utils.ElementContextLocator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class ContextLocatingElementHandler extends LocatingWithRetriesHandler {

    protected final int index;

    public ContextLocatingElementHandler(ElementContextLocator locator, Duration duration, List<Class<? extends Throwable>> troubles) {
        this(locator, -1, duration, troubles);
    }

    public ContextLocatingElementHandler(ElementContextLocator locator, int index, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(locator, duration, troubles);
        this.index = index;
    }

    @Override
    protected Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable {
        WebElement element;
        try {
            if (index >= 0) {
                element = locator.findElements().get(index);
            } else {
                element = locator.findElement();
            }
        } catch (NoSuchElementException e) {
            if ("toString".equals(method.getName())) {
                return "Proxy element for: " + locator;
            }
            throw e;
        }
        if ("getWrappedElement".equals(method.getName())) {
            return element;
        }
        try {
            return method.invoke(element, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
