package com.mona.chatfirebaseapp;

public class InstanceMessage {
    private String message;
    private String author;

    public InstanceMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    public InstanceMessage() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
