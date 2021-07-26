package io.selenium.utils.handler;

import io.selenium.utils.ElementContextLocator;
import io.selenium.utils.ElementContextLocatorFactory;
import io.selenium.utils.FieldContextDecorator;
import io.selenium.utils.WebContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.lang.reflect.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class LocatingWebContextListHandler extends LocatingWithRetriesHandler {

    protected Class<?> listType;

    public LocatingWebContextListHandler(Field field, ElementContextLocator locator, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(locator, duration, troubles);
        Type genericType = field.getGenericType();
        this.listType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    @Override
    protected Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable {
        List<WebElement> proxyElements = locator.findProxyElements();
        List<WebContext> webContexts = new ArrayList<>();
        try {
            for (WebElement proxyElement : proxyElements) {
                WebContext context = (WebContext) listType.getConstructor().newInstance();
                PageFactory.initElements(new FieldContextDecorator(new ElementContextLocatorFactory(proxyElement, duration, troubles)), context);
                webContexts.add(context);
            }
            return method.invoke(webContexts, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
