package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageStatus {
    private Long messageId;
    private MessageStatusEnum messageStatus;
}
