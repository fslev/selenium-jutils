package io.selenium.utils.handler;

import io.selenium.utils.ElementContextLocator;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;

public class ContextLocatingElementListHandler extends LocatingWithRetriesHandler {

    public ContextLocatingElementListHandler(ElementContextLocator locator, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(locator, duration, troubles);
    }

    @Override
    protected Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable {
        List<WebElement> proxyElements = locator.findProxyElements();
        try {
            return method.invoke(proxyElements, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
