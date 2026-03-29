package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.*;
import com.thinhtran.chatapp.domain.request.ReqCreateConversationDto;
import com.thinhtran.chatapp.domain.response.ResConversationDto;
import com.thinhtran.chatapp.domain.response.ResConversationMemberDto;
import com.thinhtran.chatapp.domain.response.ResSidebarDto;
import com.thinhtran.chatapp.repository.*;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import com.thinhtran.chatapp.util.constant.RoleMemberEnum;
import com.thinhtran.chatapp.util.constant.RoomTypeEnum;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final MessageFileRepository messageFileRepository;
    private final MessageStatusRepository  messageStatusRepository;
    public ConversationService(ConversationRepository conversationRepository, UserRepository  userRepository,  MemberRepository memberRepository,  MessageRepository messageRepository, MessageFileRepository messageFileRepository, MessageStatusRepository  messageStatusRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
        this.messageFileRepository = messageFileRepository;
        this.messageStatusRepository = messageStatusRepository;
    }

    public Conversation getUserById(Long id){
        return this.conversationRepository.findById(id).orElse(null);
    }

    @Transactional
    public ResConversationDto createConversation (ReqCreateConversationDto reqCreateConversationDto){
        // create conversation
        Conversation conversation = new Conversation();
        conversation.setRoomType(reqCreateConversationDto.getRoomType());
        conversation.setName(
                RoomTypeEnum.DIRECT.equals(reqCreateConversationDto.getRoomType())
                        ? null :  reqCreateConversationDto.getRoomName()
        );

        Optional<Long> creatorId = SecurityUtil.getCurrentUserId();
        if (creatorId.isEmpty()) {
            throw new IllegalArgumentException("Creator id cannot be null");
        }

        Optional<User> creator = this.userRepository.findById(creatorId.get());
        if (!creator.isPresent()) {
            throw new RuntimeException("Creator not found");
        }
        conversation.setCreatedBy(creator.get());


        // create member
        Set<Long> memIds = new HashSet<>(reqCreateConversationDto.getMemIds());
        memIds.add(creatorId.get());
        for (Long userId : memIds) {
            Optional<User> user = this.userRepository.findById(userId);
            if (!creator.isPresent()) {
                throw new RuntimeException("User not found");
            }

            Member member = new Member();
            member.setUser(user.get());
            member.setConversation(conversation);
            if (userId.equals(creatorId.get())) {
                member.setRole(
                        reqCreateConversationDto.getRoomType() == RoomTypeEnum.DIRECT ? RoleMemberEnum.MEMBER : RoleMemberEnum.ADMIN
                );
            }
            else
                member.setRole(RoleMemberEnum.MEMBER);
            conversation.getMembers().add(member);
            this.memberRepository.save(member);
        }
        this.conversationRepository.save(conversation);


        // return the response of conversation
        ResConversationDto res = new ResConversationDto();
        res.setId(conversation.getId());
        res.setRoomType(reqCreateConversationDto.getRoomType());
        res.setName(reqCreateConversationDto.getRoomName());

        return res;
    }

    public List<ResSidebarDto> getSideBar(Long userId){
        List<Conversation> conversations = this.conversationRepository.findByUserId(userId);

        List<ResSidebarDto> res = new ArrayList<>();

        for (Conversation conversation :  conversations) {
            // unread count
            Long unreadCount = this.messageStatusRepository.findUnreadMessageCount(conversation.getId(), userId);

            // last message
            Message lastMessage = messageRepository
                    .findTopByConversationIdOrderByCreatedAtDesc(conversation.getId());

            String lastMessageContent = null;
            MessageTypeEnum lastType = null;
            String fileName = null;
            String fileUrl = null;

            if (lastMessage != null) {

                lastType = lastMessage.getMessageType();

                switch (lastType) {

                    case TEXT:
                        lastMessageContent = lastMessage.getContent();
                        break;

                    case IMAGE:
                        lastMessageContent = "[Image]";
                        break;

                    case FILE:
                        MessageFile file = messageFileRepository
                                .findTopByMessageId(lastMessage.getId());

                        if (file != null) {
                            fileName = file.getFileName();
                            fileUrl = file.getFileUrl();
                            lastMessageContent = file.getFileName();
                        } else {
                            lastMessageContent = "[File]";
                        }
                        break;
                }

            }

            // name + avatar
            String name;
            String avatar;
            if (conversation.getRoomType() == RoomTypeEnum.DIRECT) {
                User otherUser = conversation.getMembers().stream().map(Member::getUser).filter(u -> !u.getId().equals(userId)).findFirst().orElse(null);

                name = otherUser != null ? otherUser.getUsername() : "Unknown";
                avatar = otherUser != null ? otherUser.getAvatar() : null;
            } else {
                name = conversation.getName();
                avatar = conversation.getAvatar();
            }


            res.add(new ResSidebarDto(
                    conversation.getId(),
                    name,
                    avatar,
                    lastMessageContent,
                    lastType,
                    fileName,
                    fileUrl,
                    lastMessage != null ? lastMessage.getCreatedAt() : null,
                    unreadCount,
                    conversation.getRoomType() == RoomTypeEnum.GROUP
            ));
        }

        return res;
    }

    public Conversation getConversationById(Long conversationId) {
        return this.conversationRepository.findById(conversationId).orElse(null);
    }
}
