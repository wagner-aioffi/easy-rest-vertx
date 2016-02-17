package com.wma.vertx.example.api;

import com.wma.vertx.kernel.annotation.ApiPath;
import com.wma.vertx.kernel.annotation.ApiOperation;
import com.wma.vertx.kernel.annotation.PathParam;
import com.wma.vertx.kernel.annotation.Sync;

import java.util.concurrent.CompletableFuture;

@ApiPath("/hello")
public class HelloWordAPI {

    @Sync
    @ApiOperation(path="/sync/:name", method = ApiOperation.Method.GET)
    public Message getMessageSync(@PathParam("name") final String name) {
        return new Message(name);
    }

    @ApiOperation(path="/async/:name", method = ApiOperation.Method.GET)
    public CompletableFuture<Message> getMessage(@PathParam("name") final String name) {
        return CompletableFuture.supplyAsync(() -> new Message(name));
    }

}
