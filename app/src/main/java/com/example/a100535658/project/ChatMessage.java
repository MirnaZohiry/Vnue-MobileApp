package com.example.a100535658.project;

import java.lang.ref.SoftReference;
import java.util.Date;

/**
 * Created by 100535658 on 12/6/2017.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String messageReceiver;
    private long messageTime;

    public ChatMessage(){

    }

    public ChatMessage(String messageText, String messageUser, String messageReceiver){
        this.setMessageText(messageText);
        this.setMessageUser(messageUser);
        this.setMessageReceiver(messageReceiver);

        setMessageTime(new Date().getTime());
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageReceiver() {
        return messageReceiver;
    }

    public void setMessageReceiver(String messageReceiver) {
        this.messageReceiver = messageReceiver;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}