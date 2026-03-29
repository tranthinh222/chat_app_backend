package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageFullDto {
    private Long messageId;

    private ResSenderDto sender;

    private String content;
    private MessageTypeEnum messageType;

    private MessageStatusEnum status;
    private Instant deliveredAt;
    private Instant seenAt;

    // reaction
    private List<ResMessageReactionDetailDto> reactions;
    // file
    private List<ResMessageFileDto> files;
}
