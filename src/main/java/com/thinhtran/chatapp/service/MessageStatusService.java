package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.MessageStatus;
import com.thinhtran.chatapp.repository.MessageStatusRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageStatusService {
    private final MessageStatusRepository messageStatusRepository;
    public MessageStatusService(MessageStatusRepository messageStatusRepository){
        this.messageStatusRepository = messageStatusRepository;
    }

    public Void markAsDelivered(Long messageId, Long userId){
        MessageStatus messageStatus = this.messageStatusRepository.findByMessageIdAndUserId(messageId, userId);
        messageStatus.setDeliveredAt(Instant.now());
        this.messageStatusRepository.save(messageStatus);

        return null;
    }

    public Void markAsSeen(Long messageId, Long userId){
        MessageStatus messageStatus = this.messageStatusRepository.findByMessageIdAndUserId(messageId, userId);
        messageStatus.setSeenAt(Instant.now());
        this.messageStatusRepository.save(messageStatus);

        return null;
    }
}
