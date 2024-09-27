package com.elixr.ChatApp_Message.model;

import com.elixr.ChatApp_Message.contants.MessageAppConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = MessageAppConstants.MESSAGE_COLLECTION)
public class MessageModel {

    @Id
    private UUID id;
    private String message;
    private String timeStamp;
    private String senderUserName;
    private String receiverUserName;
}
