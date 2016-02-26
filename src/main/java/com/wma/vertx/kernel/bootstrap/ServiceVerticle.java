package com.wma.vertx.kernel.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.wma.vertx.kernel.api.APIMethodBindConfigurationHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ServiceVerticle extends AbstractVerticle {

    private final AbstractServiceModule serviceModule;

    protected ServiceVerticle(final AbstractServiceModule serviceModule) {
        this.serviceModule = serviceModule;
    }

    @Override
    public void start(Future<Void> fut) {
        final Router router = configureRoute();
        configureGuice(router);
        configureVertex(fut, router);
    }

    private void configureGuice(final Router router) {
        final Module routerModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(Router.class).toInstance(router);
            }
        };

        Injector injector = Guice.createInjector(routerModule, serviceModule);
        instantiateApis(injector, router);
    }

    private void instantiateApis(final Injector injector, final Router router) {
        for(final Class<?> clazz : serviceModule.getApis()) {
            log.info("Instantiating API: " + clazz.getName());
            Object apiImplInstance = injector.getInstance(clazz);
            log.info("Configuring API bindings: " + clazz.getName());
            new APIMethodBindConfigurationHandler(apiImplInstance).configure(router);
        }
    }

    private Router configureRoute() {
        // Create a router object.
        Router router = Router.router(vertx);

        //TODO configure what to add by default to the router
        // Bind "/" to our hello message.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("Operation not recognized");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));
        return router;
    }

    private void configureVertex(Future<Void> fut, Router router) {
        // Create the HTTP server and pass the "accept" method to the request handler.
        final int port = Integer.getInteger("http.port");
        final String address = System.getProperty("http.address");
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        port,
                        address,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );
    }
}
