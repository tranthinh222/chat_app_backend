package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.repository.ConversationRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    public ConversationService(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Conversation getUserById(Long id){
        return this.conversationRepository.findById(id).orElse(null);
    }
}
