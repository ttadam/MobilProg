package com.mobilprog.tadam.mobilprog.Model;

/**
 * Created by thoma on 2017. 04. 29..
 */

public class Message {
    private String uid;
    private String sender;
    private String message;
    private String receiver;

    public Message() {

    }

    //Constructor for plain text message
    public Message(String uid, String sender, String message, String receiver) {
        this.uid = uid;
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getUid() { return uid;  }

    public String getReceiver() {  return receiver; }
}