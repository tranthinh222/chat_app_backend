package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.Member;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.domain.request.ReqCreateMemberDto;
import com.thinhtran.chatapp.domain.response.ResConversationMemberDto;
import com.thinhtran.chatapp.repository.ConversationRepository;
import com.thinhtran.chatapp.repository.MemberRepository;
import com.thinhtran.chatapp.repository.UserRepository;
import com.thinhtran.chatapp.util.constant.RoleMemberEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    public MemberService(MemberRepository memberRepository,  ConversationRepository conversationRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }


    public List<ResConversationMemberDto> getMemberByConversationId (Long id){
        return this.memberRepository.findMemberByConversationId(id);
    }

    public Member createMember(ReqCreateMemberDto reqCreateMemberDto){
        Conversation conversation = this.conversationRepository.findById(reqCreateMemberDto.getConversationId()).get();

        User user = this.userRepository.findById(reqCreateMemberDto.getUserId()).get();

        Member member = new Member();
        member.setConversation(conversation);
        member.setUser(user);
        member.setRole(RoleMemberEnum.MEMBER);
        this.memberRepository.save(member);
        conversation.getMembers().add(member);
        conversationRepository.save(conversation);

        return member;
    }

}
