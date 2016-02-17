package com.wma.vertx.kernel.logger;

import io.vertx.core.spi.logging.LogDelegate;
import io.vertx.core.spi.logging.LogDelegateFactory;

/**
 * A {@link LogDelegateFactory} which creates {@link Log4j2LogDelegate} instances.
 *
 * @author <a href="anidotnet@gmail.com">anidotnet@gmail.com</a>
 *
 *
 */
public class Log4j2LogDelegateFactory implements LogDelegateFactory {
    @Override
    public LogDelegate createDelegate(final String name) {
        return new Log4j2LogDelegate(name);
    }
}