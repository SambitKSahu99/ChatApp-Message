package com.elixr.ChatApp_Message.exceptionhandler;

public class MessageNotFoundException extends Exception {
    public MessageNotFoundException(String message) {
        super(message);
    }
}
