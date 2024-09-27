package com.elixr.ChatApp_Message.controller;

import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import com.elixr.ChatApp_Message.contants.MessageConstants;
import com.elixr.ChatApp_Message.contants.UrlConstants;
import com.elixr.ChatApp_Message.dto.MessageDto;
import com.elixr.ChatApp_Message.exceptionhandler.MessageException;
import com.elixr.ChatApp_Message.exceptionhandler.MessageNotFoundException;
import com.elixr.ChatApp_Message.exceptionhandler.MessageUserNotFoundException;
import com.elixr.ChatApp_Message.response.Response;
import com.elixr.ChatApp_Message.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(MessageAppConstants.ALLOWED_HEADERS)
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(UrlConstants.MESSAGE_ENDPOINT)
    public ResponseEntity<Response> saveMessage(@RequestBody MessageDto messageDto) throws MessageException, MessageUserNotFoundException {
        messageService.saveMessage(messageDto);
        return new ResponseEntity<>(new Response(MessageConstants.MESSAGE_INSERTION_SUCCESSFUL), HttpStatus.OK);
    }

    @GetMapping(UrlConstants.MESSAGE_ENDPOINT)
    public ResponseEntity<Response> getMessage() throws MessageNotFoundException {
        return new ResponseEntity<>(new Response(messageService.getMessage()),HttpStatus.OK);
    }

    @PutMapping(UrlConstants.MESSAGE_ENDPOINT)
    public ResponseEntity<String> updateMessages(@RequestParam String oldName,
                                                 @RequestParam String newName){
        messageService.updateMessages(oldName,newName);
        return new ResponseEntity<>(MessageConstants.UPDATED_SUCCESSFULLY,HttpStatus.OK);
    }
}
