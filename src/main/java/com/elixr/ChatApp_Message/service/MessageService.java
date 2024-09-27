package com.elixr.ChatApp_Message.service;

import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import com.elixr.ChatApp_Message.contants.MessageConstants;
import com.elixr.ChatApp_Message.contants.UrlConstants;
import com.elixr.ChatApp_Message.dto.MessageDto;
import com.elixr.ChatApp_Message.exceptionhandler.MessageException;
import com.elixr.ChatApp_Message.exceptionhandler.MessageNotFoundException;
import com.elixr.ChatApp_Message.exceptionhandler.MessageUserNotFoundException;
import com.elixr.ChatApp_Message.filter.JwtFilter;
import com.elixr.ChatApp_Message.model.MessageModel;
import com.elixr.ChatApp_Message.repository.MessageRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final WebClient webClient;
    private final JwtFilter jwtFilter;
    private final MongoTemplate mongoTemplate;

    public MessageService(MessageRepository messageRepository, WebClient.Builder webClientBuilder, JwtFilter jwtFilter, MongoTemplate mongoTemplate) {
        this.messageRepository = messageRepository;
        this.webClient = webClientBuilder.baseUrl(UrlConstants.USER_SERVICE_URL).build();
        this.jwtFilter = jwtFilter;
        this.mongoTemplate = mongoTemplate;
    }

    public void saveMessage(MessageDto messageDto) throws MessageException, MessageUserNotFoundException {
        if (messageDto.getMessage().length()> MessageAppConstants.MAXIMUM_CHARACTER_LIMIT){
            throw new MessageException(MessageConstants.MAXIMUM_ALLOWED_CHARACTER_MESSAGE);
        }
        if(!verifyReceiver(messageDto.getReceiverUserName())){
            throw new MessageUserNotFoundException(MessageConstants.RECEIVER_NOT_FOUND);
        }
        SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(MessageAppConstants.DATE_FORMAT);
        String formattedDate = simpleDateFormatter.format(Date.from(Instant.now()));
        String senderUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        MessageModel messageModel = MessageModel.builder()
                .id(UUID.randomUUID())
                .senderUserName(senderUserName)
                .receiverUserName(messageDto.getReceiverUserName())
                .message(messageDto.getMessage())
                .timeStamp(formattedDate)
                .build();
        messageRepository.save(messageModel);
    }

    public List<MessageDto> getMessage() throws MessageNotFoundException {
        String senderUserName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<MessageModel> messageModelOption = messageRepository
                .findBySenderUserNameOrReceiverUserName(senderUserName,senderUserName);
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

    private boolean verifyReceiver(String userName){
        return Boolean.TRUE.equals(webClient.post()
                        .uri(UrlConstants.VERIFY_USER_ENDPOINT)
                        .header(MessageAppConstants.AUTHORIZATION_HEADER
                                , MessageAppConstants.BEARER + jwtFilter.getJwtToken())
                        .bodyValue(userName)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .onErrorResume(throwable -> Mono.error(new MessageUserNotFoundException(MessageConstants.RECEIVER_NOT_FOUND)))
                        .block());
    }

    public void updateMessages(String oldName, String newName) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where(MessageAppConstants.SENDER_USERNAME).is(oldName),
                Criteria.where(MessageAppConstants.RECEIVER_USERNAME).is(oldName)
        ));

        List<MessageModel> messages = mongoTemplate.find(query, MessageModel.class, MessageAppConstants.MESSAGE_COLLECTION);

        for (MessageModel message : messages) {
            if (message.getSenderUserName().equals(oldName)) {
                message.setSenderUserName(newName);
            }
            if (message.getReceiverUserName().equals(oldName)) {
                message.setReceiverUserName(newName);
            }
            mongoTemplate.save(message);
        }
    }
}
