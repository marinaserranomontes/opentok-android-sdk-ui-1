package com.opentok.android.ui.textchat;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChatMessage {

    public static enum MessageStatus {
        SENT_MESSAGE,
        RECEIVED_MESSAGE
    }

    protected String sender;
    protected String text;
    protected long timestamp;
    protected MessageStatus status;
    private UUID id;

    public ChatMessage(String sender, String text) {

        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
        this.text = text;
        this.id= UUID.randomUUID();
    }

    public ChatMessage(String sender, String text, MessageStatus status) {
        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
        this.text = text;
        this.id= UUID.randomUUID();
        this.status = status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {

        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public void setTimestamp(long time) {
        timestamp = time;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public UUID getId() {
        return id;
    }

}
