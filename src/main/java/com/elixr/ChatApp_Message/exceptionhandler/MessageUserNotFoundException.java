package com.elixr.ChatApp_Message.exceptionhandler;

public class MessageUserNotFoundException extends Exception{
    public MessageUserNotFoundException(String message){
        super(message);
    }
}
