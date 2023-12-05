package io.selenium.utils.handler;

import io.selenium.utils.ElementContextLocator;
import io.selenium.utils.Retry;
import org.awaitility.pollinterval.FixedPollInterval;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

public abstract class LocatingWithRetriesHandler extends Retry implements InvocationHandler {

    protected final ElementContextLocator locator;

    protected LocatingWithRetriesHandler(ElementContextLocator locator, Duration duration, List<Class<? extends Throwable>> troubles) {
        super(duration, troubles);
        this.locator = locator;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        AtomicReference<Object> result = new AtomicReference<>();
        AtomicReference<Throwable> uncaughtTrouble = new AtomicReference<>();

        await().pollDelay(Duration.ZERO)
                .pollInterval(FixedPollInterval.fixed(Duration.ofMillis(POLLING_INTERVAL_MILLIS)))
                .atMost(duration != null ? duration : Duration.ofSeconds(30)).pollInSameThread()
                .untilAsserted(() -> {
                    try {
                        result.set(proxyInvoke(object, method, objects));
                    } catch (Throwable t) {
                        if (troubles.contains(t.getClass())) {
                            LOG.info("Caught throwable {}. Retry element invocation...", t.getClass().getCanonicalName());
                            throw t;
                        }
                        uncaughtTrouble.set(t);
                    }
                });
        if (uncaughtTrouble.get() != null) {
            throw uncaughtTrouble.get();
        }
        return result.get();
    }

    protected abstract Object proxyInvoke(Object object, Method method, Object[] objects) throws Throwable;
}
