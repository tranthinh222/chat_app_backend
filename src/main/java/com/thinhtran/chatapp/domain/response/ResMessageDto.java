package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageDto {
    private Long id;
    private Long userId;
    private String username;
    private String avatar;
    private MessageTypeEnum messageType;
    private String content;
    private Instant createdAt;
}
