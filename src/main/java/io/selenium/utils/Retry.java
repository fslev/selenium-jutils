package io.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

public abstract class Retry {
    protected static final Logger LOG = LogManager.getLogger();
    protected static final long POLLING_INTERVAL_MILLIS = 250L;

    protected final List<Class<? extends Throwable>> troubles;
    protected final Duration duration;

    protected Retry(Duration duration, List<Class<? extends Throwable>> troubles) {
        this.duration = duration;
        this.troubles = troubles;
    }

    public List<Class<? extends Throwable>> getTroubles() {
        return troubles;
    }

    public Duration getDuration() {
        return duration;
    }
}
