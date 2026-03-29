package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageChatDto {
    private Long messageId;
    private String content;
    private MessageTypeEnum messageType;
    private ResSenderDto  sender;
    private ResMessageFileDto file;
    private Instant createdAt;
    private MessageStatusEnum status;
}
