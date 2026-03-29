package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.Member;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.domain.request.ReqCreateMemberDto;
import com.thinhtran.chatapp.domain.response.ResConversationMemberDto;
import com.thinhtran.chatapp.service.ConversationService;
import com.thinhtran.chatapp.service.MemberService;
import com.thinhtran.chatapp.service.UserService;
import com.thinhtran.chatapp.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class MemberController {
    private final MemberService memberService;
    private final ConversationService conversationService;
    private final UserService userService;
    public MemberController(MemberService memberService,  ConversationService conversationService,  UserService userService) {
        this.memberService = memberService;
        this.conversationService = conversationService;
        this.userService = userService;
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<List<ResConversationMemberDto>> fetchMemberById(@PathVariable("id") Long id){
        return ResponseEntity.status(HttpStatus.OK).body(this.memberService.getMemberByConversationId(id));
    }

    @PostMapping("/members")
    public ResponseEntity<Member> createMember(@Valid @RequestBody ReqCreateMemberDto reqCreateMemberDto){
        Conversation conversation = this.conversationService.getUserById(reqCreateMemberDto.getConversationId());
        if (conversation == null){
            throw new IdInvalidException("conversation id with " + reqCreateMemberDto.getConversationId() + " is invalid");
        }

        User user = this.userService.getUserById(reqCreateMemberDto.getUserId());
        if (user == null){
            throw new IdInvalidException("user with " + reqCreateMemberDto.getUserId() + " is invalid");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.memberService.createMember(reqCreateMemberDto));
    }
}
