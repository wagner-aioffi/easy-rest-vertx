package com.wma.vertx.kernel.logger;

import io.vertx.core.spi.logging.LogDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A {@link LogDelegate} which delegates to Apache Log4j 2.x
 *
 * @author <a href="anidotnet@gmail.com">anidotnet@gmail.com</a>
 */
public class Log4j2LogDelegate implements LogDelegate {

    private final Logger logger;

    Log4j2LogDelegate(final String name) {
        logger = LogManager.getLogger(name);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void fatal(final Object message) {
        logger.fatal(message);
    }

    @Override
    public void fatal(final Object message, final Throwable t) {
        logger.fatal(message, t);
    }

    @Override
    public void error(final Object message) {
        logger.error(message);
    }

    @Override public void error(Object message, Object... params) {
        logger.error(message.toString(), params);
    }

    @Override
    public void error(final Object message, final Throwable t) {
        logger.error(message, t);
    }

    @Override public void error(Object message, Throwable t, Object... params) {
        logger.error(message.toString(), params, t);
    }

    @Override
    public void warn(final Object message) {
        logger.warn(message);
    }

    @Override public void warn(Object message, Object... params) {
        logger.warn(message.toString(), params);
    }

    @Override
    public void warn(final Object message, final Throwable t) {
        logger.warn(message, t);
    }

    @Override public void warn(Object message, Throwable t, Object... params) {
        logger.warn(message.toString(), params, t);
    }

    @Override
    public void info(final Object message) {
        logger.info(message);
    }

    @Override public void info(Object message, Object... params) {
        logger.info(message.toString(), params);
    }

    @Override
    public void info(final Object message, final Throwable t) {
        logger.info(message, t);
    }

    @Override public void info(Object message, Throwable t, Object... params) {
        logger.info(message.toString(), params, t);
    }

    @Override
    public void debug(final Object message) {
        logger.debug(message);
    }

    @Override public void debug(Object message, Object... params) {
        logger.debug(message.toString(), params);
    }

    @Override
    public void debug(final Object message, final Throwable t) {
        logger.debug(message, t);
    }

    @Override public void debug(Object message, Throwable t, Object... params) {
        logger.debug(message.toString(), params, t);
    }

    @Override
    public void trace(final Object message) {
        logger.trace(message);
    }

    @Override public void trace(Object message, Object... params) {
        logger.trace(message.toString(), params);
    }

    @Override
    public void trace(final Object message, final Throwable t) {
        logger.trace(message, t);
    }

    @Override public void trace(Object message, Throwable t, Object... params) {
        logger.trace(message.toString(), params, t);
    }
}