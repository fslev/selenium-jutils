package io.selenium.utils.handler;

import io.jtest.utils.polling.Polling;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ElementInvocationWithRetriesHandler implements InvocationHandler {

    protected static final Logger LOG = LogManager.getLogger();

    protected final ElementLocator locator;
    protected final Duration duration;
    protected final Set<Class<? extends Throwable>> troubles;

    public ElementInvocationWithRetriesHandler(ElementLocator locator, Duration duration, Set<Class<? extends Throwable>> troubles) {
        this.locator = locator;
        this.duration = duration;
        this.troubles = troubles;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        AtomicReference<Object> result = new AtomicReference<>();
        Throwable throwable = new Polling<Throwable>().duration(duration, 250L)
                .supplier(() -> {
                    try {
                        result.set(proxyInvoke(object, method, objects));
                        return null;
                    } catch (Throwable t) {
                        if (troubles.contains(t.getClass())) {
                            LOG.info("Caught throwable {}. Retry element invocation...", t.getClass().getCanonicalName());
                        }
                        return t;
                    }
                }).until(t -> t == null || !troubles.contains(t.getClass())).get();

        if (throwable == null) {
            return result.get();
        }
        throw throwable;
    }

    private Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable {
        WebElement element;
        try {
            element = locator.findElement();
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
