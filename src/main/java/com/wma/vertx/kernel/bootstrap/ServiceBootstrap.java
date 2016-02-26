package com.wma.vertx.kernel.bootstrap;

import io.vertx.core.Vertx;

public class ServiceBootstrap {

    public static void start(final AbstractServiceModule module) {
        final Vertx vertx = Vertx.vertx();

        final ServiceVerticle verticle = new ServiceVerticle(module);

        vertx.deployVerticle(verticle);
    }
}
