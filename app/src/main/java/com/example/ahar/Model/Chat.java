package com.example.ahar.Model;
/**
 * Created by Istiak Saif on 16/04/21.
 */
public class Chat {

    private String sender, receiver, message,time,date;

    public Chat() {
    }

    public Chat(String sender, String receiver, String message, String time, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.time = time;
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
