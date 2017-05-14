package com.mobilprog.tadam.mobilprog.Model;

/**
 * Created by thoma on 2017. 04. 29..
 */

public class Message {
    private String sender;
    private String message;

    public Message() {

    }

    //Constructor for plain text message
    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}