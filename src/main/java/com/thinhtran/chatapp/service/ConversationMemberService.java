package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.repository.ConversationMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class ConversationMemberService {
    private final ConversationMemberRepository conversationMemberRepository;
    public ConversationMemberService(ConversationMemberRepository conversationMemberRepository) {
        this.conversationMemberRepository = conversationMemberRepository;
    }


}
