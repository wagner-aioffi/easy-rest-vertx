package com.wma.vertx.kernel.api;

import com.google.common.base.Stopwatch;
import com.wma.vertx.kernel.annotation.ApiOperation;
import com.wma.vertx.kernel.annotation.PathParam;
import com.wma.vertx.kernel.annotation.Sync;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
public class OperationMethodBind {

    private Vertx vertx;

    private String apiPrefix;

    private final ApiOperation apiOperation;
    private final Method method;
    private ParameterBindConfiguration[] argsConfig;

    public OperationMethodBind(final String apiPrefix, ApiOperation apiOperation, Method method) {
        this.apiPrefix = apiPrefix;
        this.apiOperation = apiOperation;
        this.method = method;
        configureParamMap();
    }

    public void bind(final Object apiInstance, final Router router) {
        final String fullPath = apiPrefix + apiOperation.path();
        log.info(String.format("Binding @ApiOperation(%s) method: %s", fullPath, apiOperation.method()));

        Route route = getRouteByMethod(router, fullPath);

        if (method.isAnnotationPresent(Sync.class)) {
            route.blockingHandler(createSyncHandler(apiInstance));
        } else {
            route.handler(createAsyncHandler(apiInstance));
        }
    }

    private Route getRouteByMethod(Router router, String fullPath) {
        switch (apiOperation.method()) {
            case GET: return router.get(fullPath);
            case POST: return router.post(fullPath);
            case PUT: return router.put(fullPath);
            default: throw new RuntimeException("Method not recognized");
        }
    }

    private Handler<RoutingContext> createSyncHandler(Object apiInstance) {
        final Handler<RoutingContext> handler = context -> {
            Stopwatch invokeTimer = Stopwatch.createStarted();
            final Object[] args = buildInvokeArgs(context);
            try {
                final Object result = method.invoke(apiInstance, args);
                invokeTimer.stop();
                log.info("Invoke time: " + invokeTimer.toString());
                final HttpServerResponse response = context.response();
                final String responseStr = result.toString();
                response.putHeader("content-type", "text/plain");
                response.putHeader("Content-Length", Integer.toString(responseStr.length()));
                response.write(responseStr);
                response.end();
            } catch (Exception e) {
                throw new RuntimeException("FIXME: improve ex. handler", e);
            }
        };
        return handler;
    }

    private Handler<RoutingContext> createAsyncHandler(Object apiInstance) {
        final Handler<RoutingContext> handler = context -> {
            Stopwatch invokeTimer = Stopwatch.createStarted();
            final Object[] args = buildInvokeArgs(context);
            try {
                final CompletableFuture<Object> futureResult =
                        (CompletableFuture<Object>) method.invoke(apiInstance, args);
                invokeTimer.stop();
                log.info("Invoke time: " + invokeTimer.toString());

                futureResult.thenAccept(result -> {
                    final HttpServerResponse response = context.response();
                    final String responseStr = result.toString();
                    response.putHeader("content-type", "text/plain");
                    response.putHeader("Content-Length",
                            Integer.toString(responseStr.length()));
                    response.write(responseStr);
                    response.end();
                });
            } catch (Exception e) {
                throw new RuntimeException("FIXME: improve ex. handler", e);
            }
        };
        return handler;
    }

    private Object[] buildInvokeArgs(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        final Object[] values = new Object[argsConfig.length];

        final Stopwatch argCreationTimer = Stopwatch.createStarted();
        for (int i = 0; i < argsConfig.length; i++) {
            final ParameterBindConfiguration paramConfig = argsConfig[i];
            //TODO convert type
            final String param = request.getParam(paramConfig.paramName);
            values[i] = param;
        }
        argCreationTimer.stop();
        log.info("Arg creation time: " + argCreationTimer.toString());
        return values;
    }

    private void configureParamMap() {
        final List<ParameterBindConfiguration> params = new ArrayList<>();
        final Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Optional<ParameterBindConfiguration> parameterBindConfiguration = configurePathParam(i, parameters[i]);
            if (parameterBindConfiguration.isPresent()) {
                params.add(parameterBindConfiguration.get());
                continue;
            } else {
                final String msg =String.format(
                        "API %s parameter %s need to be configured with annotation. (@PathParam)",
                        method.getDeclaringClass().getName(),
                        apiOperation.method());
                log.error(msg);
                throw new RuntimeException(msg);
            }
        }
        argsConfig = params.toArray(new ParameterBindConfiguration[0]);
    }

    private Optional<ParameterBindConfiguration> configurePathParam(final int order, final Parameter parameter) {
        PathParam annotation = parameter.getAnnotation(PathParam.class);
        if (annotation != null) {
            return Optional.of(
                    new ParameterBindConfiguration(annotation.value(), order,
                            parameter));
        }
        return Optional.empty();
    }

    private static class ParameterBindConfiguration {

        private final String paramName;
        private int order;
        private Parameter parameter;

        private ParameterBindConfiguration(final String paramName, final int order, final Parameter parameter) {
            this.paramName = paramName;
            this.order = order;
            this.parameter = parameter;
            log.info(String.format("Binding argument [%s] to @PathParam(%s)", parameter, paramName));
        }
    }
}
