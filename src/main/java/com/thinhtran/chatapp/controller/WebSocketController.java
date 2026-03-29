package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.request.ReqMessageChatDto;
import com.thinhtran.chatapp.domain.response.ResMessageChatDto;
import com.thinhtran.chatapp.domain.response.ResMessageFullDto;
import com.thinhtran.chatapp.domain.response.ResMessageStatus;
import com.thinhtran.chatapp.service.MessageService;
import com.thinhtran.chatapp.service.MessageStatusService;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final MessageStatusService messageStatusService;

    @MessageMapping("/chat.send")
    public void handleGroupChat(@Payload @Valid ReqMessageChatDto message) {
        ResMessageChatDto savedMessage = this.messageService.sendGroupMessage(message, SecurityUtil.getCurrentUserId().orElseThrow());

        this.messagingTemplate.convertAndSend("/topic/conversations/" + message.getConversationId(), savedMessage);
    }

    @MessageMapping("/chat.direct")
    public void handleDirectChat(@Payload @Valid ReqMessageChatDto request, Principal principal) {
        Long senderId = Long.parseLong(principal.getName());

        this.messageService.sendDirectMessage(request, senderId);
    }

    @MessageMapping("/chat.delivered")
    public void handleDelivered(Long messageId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        messageStatusService.markAsDelivered(messageId, userId);

        Long senderId = this.messageService.findSenderIdByMessageId(messageId);
        messagingTemplate.convertAndSendToUser(senderId.toString(), "/queue/status", new ResMessageStatus(messageId, MessageStatusEnum.DELIVERED));

    }

    @MessageMapping("/chat.seen")
    public void handleSeen(Long messageId, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        messageStatusService.markAsSeen(messageId, userId);

        Long senderId = this.messageService.findSenderIdByMessageId(messageId);
        messagingTemplate.convertAndSendToUser(senderId.toString(), "/queue/status", new ResMessageStatus(messageId, MessageStatusEnum.SEEN));

    }
}
