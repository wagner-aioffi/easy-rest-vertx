package com.wma.vertx.kernel.api;

import com.wma.vertx.kernel.annotation.ApiOperation;
import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Method;

public class OperationMethodGetBind extends OperationMethodBind {

    public OperationMethodGetBind(final String apiPrefix, final ApiOperation apiOperation,
            final Method method) {
        super(apiPrefix, apiOperation, method);

    }

}
