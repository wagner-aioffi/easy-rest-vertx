package com.wma.vertx.example;

import com.wma.vertx.kernel.bootstrap.AbstractServiceVerticle;

public class ServiceVerticle extends AbstractServiceVerticle {

    public ServiceVerticle() {
        super(new ServiceModule());
    }
}
