package com.elixr.ChatApp_Message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private String message;
    private String receiverUserName;
    private String senderUserName;
    private String timeStamp;
}
