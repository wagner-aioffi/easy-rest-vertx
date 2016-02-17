package com.wma.vertx.example;

import com.google.inject.AbstractModule;
import com.wma.vertx.example.api.HelloWordAPI;
import com.wma.vertx.kernel.bootstrap.AbstractServiceModule;

public class ServiceModule extends AbstractServiceModule {

    @Override
    protected void configure() {
        configureApi(HelloWordAPI.class);
    }

}
