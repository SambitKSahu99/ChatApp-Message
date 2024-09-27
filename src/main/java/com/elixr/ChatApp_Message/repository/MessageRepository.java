package com.elixr.ChatApp_Message.repository;

import com.elixr.ChatApp_Message.model.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends MongoRepository<MessageModel, UUID> {
    List<MessageModel> findBySenderUserNameOrReceiverUserName(String senderUserName, String receiverUserName );
}
