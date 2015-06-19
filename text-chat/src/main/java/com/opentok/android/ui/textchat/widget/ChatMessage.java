package com.opentok.android.ui.textchat.widget;

import java.util.UUID;

/**
* Defines the chat message object that you pass into the
* {@link com.opentok.android.ui.textchat.widget.TextChatFragment#addMessage(ChatMessage msg)}
* method.
*/
public class ChatMessage {

    /**
     * Defines the status of the message (whether it was a sent or received message).
     */
    public static enum MessageStatus {
        /**
         * The status for a sent message.
         */
        SENT_MESSAGE,
        /**
         * The status for a received message.
         */
        RECEIVED_MESSAGE
    }

    protected String sender;
    protected String text;
    protected long timestamp;
    protected MessageStatus status;
    private UUID id;

    /**
    * Construct a chat message that includes only a message string.
    *
    * @param text The text of the message.
    */
    public ChatMessage(String text) {
        this.text = text;
        this.id= UUID.randomUUID();
    }

    /**
    * Construct a chat message that includes a message string and a sender identifier.
    *
    * @param sender The string identifying the sender of the message.
    *
    * @param text The text of the message.
    */
    public ChatMessage(String sender, String text) {
        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
        this.text = text;
        this.id= UUID.randomUUID();
    }

    /**
    * Construct a chat message that includes a message string, a sender identifier,
    * and the sent/recieved status.
    *
    * @param sender The string identifying the sender of the message.
    *
    * @param text The text of the message.
    *
    * @param status Whether the message was sent or received.
    */
    public ChatMessage(String sender, String text, MessageStatus status) {
        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
        this.text = text;
        this.id= UUID.randomUUID();
        this.status = status;
    }

    /**
     * Returns the sender identifier of the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender identifier of the message.
     */
    public void setSender(String sender) {
        if ( sender == null ) {
            throw new IllegalArgumentException("The sender alias cannot be null");
        }
        this.sender = sender;
    }

    /**
     * Returns the text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the sent/received status of the message.
     */
    public MessageStatus getStatus() {
        return status;
    }

    /**
     * Sets the sent/received status of the message.
     */
    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    /**
     * Sets the UNIX timestamp for the message.
     */
    public void setTimestamp(long time) {
        timestamp = time;
    }

    /**
     * Returns the UNIX timestamp for the message.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the unique identifier for the message.
     */
    public UUID getId() {
        return id;
    }

}
