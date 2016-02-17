package com.wma.vertx.example.api;

import lombok.Data;

@Data
public class Message {

    private final String user;
    private final String message;

    public Message(String user) {
        this.user = user;
        this.message = "Hi, user";
    }
}
