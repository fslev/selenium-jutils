package io.selenium.utils.handler;

import io.jtest.utils.exceptions.PollingTimeoutException;
import io.jtest.utils.polling.Polling;
import io.selenium.utils.ElementContextLocator;
import io.selenium.utils.Retry;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public abstract class LocatingWithRetriesHandler extends Retry implements InvocationHandler {

    protected final ElementContextLocator locator;

    protected LocatingWithRetriesHandler(ElementContextLocator locator, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(duration, troubles);
        this.locator = locator;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        AtomicReference<Object> result = new AtomicReference<>();
        AtomicReference<Throwable> lastTrouble = new AtomicReference<>();
        Polling<Throwable> polling = new Polling<Throwable>().duration(duration, POLLING_INTERVAL_MILLIS)
                .supplier(() -> {
                    try {
                        result.set(proxyInvoke(object, method, objects));
                        return null;
                    } catch (Throwable t) {
                        if (troubles.contains(t.getClass())) {
                            LOG.info("Caught throwable {}. Retry element invocation...", t.getClass().getCanonicalName());
                            lastTrouble.set(t);
                        }
                        return t;
                    }
                }).until(t -> t == null || !troubles.contains(t.getClass()));
        try {
            Throwable throwable = polling.get();
            if (throwable == null) {
                return result.get();
            } else {
                throw throwable;
            }
        } catch (PollingTimeoutException e) {
            throw lastTrouble.get();
        }
    }

    protected abstract Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable;
}
