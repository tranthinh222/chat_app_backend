package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import com.thinhtran.chatapp.util.constant.RoomTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSidebarDto {
    private Long conversationId;
    private String username;
    private String avatar;

    private String lastMessage;
    private MessageTypeEnum lastMessageType;

    private String lastFileName;
    private String lastFileUrl;
    private Instant lastMessageTime;
    private Long unreadMessageCount;
    private Boolean isGroup;
}
