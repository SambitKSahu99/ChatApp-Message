package com.elixr.ChatApp_Message.service;

import com.elixr.ChatApp_Message.contants.LogInfoConstants;
import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import com.elixr.ChatApp_Message.contants.MessageConstants;
import com.elixr.ChatApp_Message.dto.MessageDto;
import com.elixr.ChatApp_Message.exceptionhandler.MessageException;
import com.elixr.ChatApp_Message.exceptionhandler.MessageNotFoundException;
import com.elixr.ChatApp_Message.filter.JwtFilter;
import com.elixr.ChatApp_Message.model.MessageModel;
import com.elixr.ChatApp_Message.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final JwtFilter jwtFilter;

    public MessageService(MessageRepository messageRepository, JwtFilter jwtFilter) {
        this.messageRepository = messageRepository;
        this.jwtFilter = jwtFilter;
    }

    public void saveMessage(MessageDto messageDto) throws MessageException {
        if (messageDto.getMessage().length()> MessageAppConstants.MAXIMUM_CHARACTER_LIMIT){
            throw new MessageException(MessageConstants.MAXIMUM_ALLOWED_CHARACTER_MESSAGE);
        }
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(MessageAppConstants.DATE_FORMAT);
        String formattedDate = simpleDateFormatter.format(Date.from(Instant.now()));
        String senderUserName = jwtFilter.getCurrentUser();
        MessageModel messageModel = MessageModel.builder()
                .id(UUID.randomUUID())
                .senderUserName(senderUserName)
                .receiverUserName(messageDto.getReceiverUserName())
                .message(messageDto.getMessage())
                .timeStamp(formattedDate)
                .build();
        messageRepository.save(messageModel);
        log.info(LogInfoConstants.SAVING_MESSAGE_IN_DB);
    }

    public List<MessageDto> getMessage() throws MessageNotFoundException {
        String senderUserName = jwtFilter.getCurrentUser();
        List<MessageModel> messageModelOption = messageRepository
                .findBySenderUserNameOrReceiverUserName(senderUserName,senderUserName);
        log.info(LogInfoConstants.RETRIEVING_MESSAGES_BY_USERNAME,senderUserName);
        if(messageModelOption.isEmpty()){
            throw new MessageNotFoundException(MessageConstants.MESSAGE_NOT_FOUND);
        }
        return messageModelOption.stream()
                .sorted(Comparator.comparing(MessageModel::getTimeStamp).reversed())
                .limit(50)
                .map(messageModel -> MessageDto.builder()
                        .message(messageModel.getMessage())
                        .timeStamp(messageModel.getTimeStamp())
                        .receiverUserName(messageModel.getReceiverUserName())
                        .senderUserName(messageModel.getSenderUserName())
                        .build())
                .collect(Collectors.toList());
    }
}
