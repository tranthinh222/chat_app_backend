package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ConversationController {
    private final ConversationService conversationService;
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }
    @GetMapping("/conversation/{id}")
    public ResponseEntity<Conversation> fetchConversationById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.conversationService.getUserById(id));
    }
}
