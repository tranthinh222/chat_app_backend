package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.service.ConversationMemberService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConversationMemberController {
    private final ConversationMemberService conversationMemberService;
    public ConversationMemberController(ConversationMemberService conversationMemberService) {
        this.conversationMemberService = conversationMemberService;
    }

    

}
