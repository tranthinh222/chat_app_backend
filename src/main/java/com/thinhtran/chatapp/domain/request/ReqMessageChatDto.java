package com.thinhtran.chatapp.domain.request;

import com.thinhtran.chatapp.domain.response.ResMessageFileDto;
import com.thinhtran.chatapp.domain.response.ResMessageReactionDto;
import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqMessageChatDto {
    @NotNull
    private Long conversationId;
    private String content;
    private MessageTypeEnum messageType;

    private ReqFileDto file;
}
