package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.request.ReqCreateConversationDto;
import com.thinhtran.chatapp.domain.response.ResConversationDto;
import com.thinhtran.chatapp.domain.response.ResConversationMemberDto;
import com.thinhtran.chatapp.domain.response.ResMessageFullDto;
import com.thinhtran.chatapp.domain.response.ResSidebarDto;
import com.thinhtran.chatapp.service.ConversationService;
import com.thinhtran.chatapp.service.MessageService;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class ConversationController {
    private final ConversationService conversationService;
    private final MessageService messageService;
    public ConversationController(ConversationService conversationService, MessageService messageService) {
        this.conversationService = conversationService;
        this.messageService = messageService;
    }
    @GetMapping("/conversations/{id}/")
    public ResponseEntity<Conversation> fetchConversationById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.conversationService.getUserById(id));
    }



    @GetMapping("/conversations/{id}/messages")
    @ApiMessage("fetch messages by conversation id")
    public ResponseEntity<Page<ResMessageFullDto>> getAllMessages(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.messageService.getMessages(id, SecurityUtil.getCurrentUserId().get(), pageable));
    }


    @PostMapping("/conversations")
    public ResponseEntity<ResConversationDto> createConversation(@Valid @RequestBody ReqCreateConversationDto reqCreateConversationDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.conversationService.createConversation(reqCreateConversationDto));
    }

    @GetMapping("/sidebar")
    public ResponseEntity<List<ResSidebarDto>> getSideBar(){
        return  ResponseEntity.status(HttpStatus.OK).body(this.conversationService.getSideBar(SecurityUtil.getCurrentUserId().get()));
    }
}
