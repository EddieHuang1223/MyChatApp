package com.chat.mychatapp;


public class Message {
    String name;
    String message;

    public Message() {
    }

    public Message(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
