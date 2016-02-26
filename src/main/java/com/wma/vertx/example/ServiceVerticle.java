package com.wma.vertx.example;

import com.wma.vertx.kernel.bootstrap.ServiceBootstrap;

public class ServiceVerticle {

    public static void main(final String[] args) {
        //debug purpose
        System.setProperty("http.port", "8080");
        System.setProperty("http.address", "localhost");

        ServiceBootstrap.start(new ServiceModule());
    }

}
