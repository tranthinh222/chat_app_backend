package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.Member;
import com.thinhtran.chatapp.domain.Message;
import com.thinhtran.chatapp.domain.MessageFile;
import com.thinhtran.chatapp.domain.MessageStatus;
import com.thinhtran.chatapp.domain.request.ReqMessageChatDto;
import com.thinhtran.chatapp.domain.response.*;
import com.thinhtran.chatapp.repository.*;
import com.thinhtran.chatapp.util.constant.MessageStatusEnum;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageStatusRepository messageStatusRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final MessageFileRepository messageFileRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MemberRepository  memberRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Long findSenderIdByMessageId(Long messageId) {
        return this.messageRepository.findSenderIdById(messageId);
    }

    public Page<ResMessageFullDto> getMessages(Long conversationId, Long userId, Pageable pageable) {

        Page<ResMessageDto> messages = messageRepository.findMesssages(conversationId, pageable);

        List<Long> messageIds = messages.getContent()
                .stream()
                .map(ResMessageDto::getId)
                .toList();

        // fetch batch
        List<MessageStatus> statuses = messageStatusRepository.findAllByMessageIds(messageIds, userId);
        List<ResMessageReactionDto> reactions = messageReactionRepository.findAllByMessageIds(messageIds);
        List<MessageFile> files = messageFileRepository.findAllByMessageIds(messageIds);

        return messages.map(msg -> {
            return buildFullMessage(msg, statuses, reactions, files);
        });
    }

    private ResMessageFullDto buildFullMessage(
            ResMessageDto msg,
            List<MessageStatus> statuses,
            List<ResMessageReactionDto> reactions,
            List<MessageFile> files) {

        ResMessageFullDto dto = new ResMessageFullDto();

        dto.setMessageId(msg.getId());

        dto.setSender(new ResSenderDto(
                msg.getUserId(),
                msg.getUsername(),
                msg.getAvatar()
        ));

        dto.setContent(msg.getContent());
        dto.setMessageType(msg.getMessageType());

        // 🔥 STATUS (theo user hiện tại)
        MessageStatus status = statuses.stream()
                .filter(s -> s.getMessage().getId().equals(msg.getId()))
                .findFirst()
                .orElse(null);

        if (status != null) {
            dto.setDeliveredAt(status.getDeliveredAt());
            dto.setSeenAt(status.getSeenAt());

            if (status.getSeenAt() != null)
                dto.setStatus(MessageStatusEnum.SEEN);
            else if (status.getDeliveredAt() != null)
                dto.setStatus(MessageStatusEnum.DELIVERED);
            else
                dto.setStatus(MessageStatusEnum.SENT);
        }

        // 🔥 REACTIONS
        List<ResMessageReactionDetailDto> reactionDtos = reactions.stream()
                .filter(r -> r.getMessageReactionId().getMessageId().equals(msg.getId()))
                .map(r -> {
                    ResMessageReactionDetailDto rDto = new ResMessageReactionDetailDto();
                    rDto.setUserId(r.getMessageReactionId().getUserId());
                    rDto.setReactionType(r.getReactionType());
                    return rDto;
                })
                .toList();

        dto.setReactions(reactionDtos);

        // 🔥 FILES
        List<ResMessageFileDto> fileDtos = files.stream()
                .filter(f -> f.getMessage().getId().equals(msg.getId()))
                .map(f -> {
                    ResMessageFileDto fDto = new ResMessageFileDto();
                    fDto.setFileUrl(f.getFileUrl());
                    fDto.setFileName(f.getFileName());
                    fDto.setFileType(f.getFileType());
                    fDto.setFileSize(f.getFileSize());
                    return fDto;
                })
                .toList();

        dto.setFiles(fileDtos);

        return dto;
    }

    @Transactional
    public ResMessageChatDto sendGroupMessage(ReqMessageChatDto dto, Long userId) {

        Message message = new Message();
        message.setConversation(conversationRepository.findById(dto.getConversationId()).get());
        message.setSender(userRepository.findById(userId).get());
        message.setMessageType(dto.getMessageType());
        message.setContent(dto.getContent());

        message = messageRepository.save(message);

        if (dto.getMessageType() == MessageTypeEnum.FILE ||
                dto.getMessageType() == MessageTypeEnum.IMAGE) {

            MessageFile file = new MessageFile();
            file.setFileName(dto.getFile().getFileName());
            file.setFileUrl(dto.getFile().getFileUrl());
            file.setFileType(dto.getFile().getFileType());
            file.setFileSize(dto.getFile().getFileSize());
            file.setMessage(message);

            messageFileRepository.save(file);
        }

        createMessageStatus(message, userId);

        return buildMessageChat(message, userId);
    }

    private void createMessageStatus(Message message, Long senderId) {

        List<Member> members = memberRepository.findByConversationId(message.getConversation().getId());

        List<MessageStatus> statuses = new ArrayList<>();

        for (Member m : members) {

            MessageStatus s = new MessageStatus();
            s.setMessage(message);
            s.setUser(m.getUser());
            statuses.add(s);
        }

        messageStatusRepository.saveAll(statuses);
    }

    //
    private ResMessageChatDto buildMessageChat(Message message, Long userId) {

        ResMessageChatDto dto = new ResMessageChatDto();

        dto.setMessageId(message.getId());
        dto.setContent(message.getContent());
        dto.setMessageType(message.getMessageType());
        dto.setCreatedAt(message.getCreatedAt());

        // sender
        dto.setSender(new ResSenderDto(
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getSender().getAvatar()
        ));

        // 🔥 file (vì bạn đang 1 message = 1 file)
        if (message.getMessageType() == MessageTypeEnum.FILE ||
                message.getMessageType() == MessageTypeEnum.IMAGE) {

            MessageFile file = messageFileRepository
                    .findByMessageId(message.getId());

            if (file != null) {
                dto.setFile(new ResMessageFileDto(
                        file.getFileUrl(),
                        file.getFileName(),
                        file.getFileType(),
                        file.getFileSize()
                ));
            }
        }

        MessageStatus status = messageStatusRepository
                .findByMessageIdAndUserId(message.getId(), userId);

        if (status != null) {
            if (status.getSeenAt() != null) {
                dto.setStatus(MessageStatusEnum.SEEN);
            } else if (status.getDeliveredAt() != null) {
                dto.setStatus(MessageStatusEnum.DELIVERED);
            } else {
                dto.setStatus(MessageStatusEnum.SENT);
            }
        }

        return dto;
    }


    // handle direct message
    @Transactional
    public Void sendDirectMessage(ReqMessageChatDto dto, Long userId) {
        Message message = new Message();
        message.setConversation(conversationRepository.findById(dto.getConversationId()).get());
        message.setSender(userRepository.findById(userId).get());
        message.setMessageType(dto.getMessageType());
        message.setContent(dto.getContent());

        message = messageRepository.save(message);

        if (dto.getMessageType() == MessageTypeEnum.FILE ||
                dto.getMessageType() == MessageTypeEnum.IMAGE) {

            MessageFile file = new MessageFile();
            file.setFileName(dto.getFile().getFileName());
            file.setFileUrl(dto.getFile().getFileUrl());
            file.setFileType(dto.getFile().getFileType());
            file.setFileSize(dto.getFile().getFileSize());
            file.setMessage(message);

            messageFileRepository.save(file);
        }

        createMessageStatus(message, userId);
        ResMessageChatDto res = buildMessageChat(message, userId);
        List<Long> memIds = this.memberRepository.findUserIdsByConversationId(dto.getConversationId());

        for (Long memId : memIds) {
            this.messagingTemplate.convertAndSendToUser(memId.toString(), "/queue/messages", res);
        }
        return null;
    }
}
