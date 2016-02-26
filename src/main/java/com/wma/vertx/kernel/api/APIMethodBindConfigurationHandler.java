package com.wma.vertx.kernel.api;

import com.wma.vertx.kernel.annotation.ApiOperation;
import com.wma.vertx.kernel.annotation.ApiPath;
import io.vertx.ext.web.Router;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

public class APIMethodBindConfigurationHandler {

    private final Object apiHandler;

    private final String apiPath;

    public APIMethodBindConfigurationHandler(final Object apiHandler) {

        this.apiHandler = apiHandler;
        apiPath = getRequiredAnnotation(ApiPath.class, apiHandler).value();
    }

    public void configure(final Router router) {

        for(final Method method : apiHandler.getClass().getDeclaredMethods()) {
            Optional<ApiOperation> operationAnnotation =
                    getOperationAnnotation(method);

            if (operationAnnotation.isPresent()) {
                addOperation(operationAnnotation.get(), method, router);
            }
        }
    }

    private void addOperation(final ApiOperation apiOperation, final Method method,
            final Router router) {
        switch (apiOperation.method()) {
            case GET:
                configureGet(apiOperation, method, router);
                break;
            case POST:
                configurePost(apiOperation, method, router);
                break;
            case PUT:
                configurePut(apiOperation, method, router);
                break;

        }
    }

    private void configurePut(final ApiOperation apiOperation, Method method,
            final Router router) {
            throw new RuntimeException("Not implemented");
    }

    private void configurePost(final ApiOperation apiOperation, Method method,
            final Router router) {
        throw new RuntimeException("Not implemented");
    }

    private void configureGet(final ApiOperation apiOperation, Method method,
            final Router router) {
        //TODO use factory
        new OperationMethodBind(apiPath, apiOperation, method).bind(apiHandler, router);
    }

    private <T extends Annotation> T getRequiredAnnotation(final Class<T> annotationClazz, final Object object) {
        T annotation = object.getClass().getAnnotation(annotationClazz);
        if (annotation == null) {
            throw new RuntimeException("Annotation " + annotationClazz.getName() + " not found on " + object.getClass().getName());
        }
        return annotation;
    }

    private Optional<ApiOperation> getOperationAnnotation(final Method method) {
        return Optional.ofNullable(method.getDeclaredAnnotation(ApiOperation.class));
    }
}
