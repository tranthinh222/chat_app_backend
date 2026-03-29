package com.thinhtran.chatapp.service;

import com.thinhtran.chatapp.domain.Message;
import com.thinhtran.chatapp.domain.MessageReaction;
import com.thinhtran.chatapp.domain.MessageReactionId;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.domain.request.ReqCreateMessageReactionDto;
import com.thinhtran.chatapp.domain.request.ReqUpdateMessageReactionDto;
import com.thinhtran.chatapp.domain.response.ResMessageReactionDto;
import com.thinhtran.chatapp.repository.MessageReactionRepository;
import com.thinhtran.chatapp.repository.MessageRepository;
import com.thinhtran.chatapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageReactionService {
    private final MessageReactionRepository messageReactionRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    public MessageReactionService(MessageReactionRepository messageReactionRepository,  UserRepository userRepository,  MessageRepository messageRepository) {
        this.messageReactionRepository = messageReactionRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public List<ResMessageReactionDto> getMessageReactionByMessageId(Long messsageId){
        return this.messageReactionRepository.findAllByMessageId(messsageId);
    }

    public Optional<MessageReaction> getMessageReactionById(MessageReactionId messageReactionId){
        return this.messageReactionRepository.findById(messageReactionId);
    }

    public MessageReaction createMessageReaction(ReqCreateMessageReactionDto reqCreateMessageReactionDto) {
        Optional<Message> message = this.messageRepository.findById(reqCreateMessageReactionDto.getId().getMessageId());

        if (!message.isPresent()) {
            throw new RuntimeException("Message not found");
        }

        Optional<User> user = this.userRepository.findById(reqCreateMessageReactionDto.getId().getUserId());
        if (!user.isPresent()) {
            throw new RuntimeException("User not found");
        }

        MessageReaction  messageReaction = new MessageReaction();
        messageReaction.setMessage(message.get());
        messageReaction.setUser(user.get());
        messageReaction.setId(reqCreateMessageReactionDto.getId());
        messageReaction.setReactionType(reqCreateMessageReactionDto.getReactionType());

        return this.messageReactionRepository.save(messageReaction);
    }

    public MessageReaction updateMessageReaction(ReqUpdateMessageReactionDto    reqMessageReaction) {
        Optional<MessageReaction> messageReaction = this.messageReactionRepository.findById(reqMessageReaction.getId());

        messageReaction.get().setReactionType(reqMessageReaction.getReactionType());
        return this.messageReactionRepository.save(messageReaction.get());
    }


    public Void deleteMessageReaction(MessageReactionId messageReactionId) {
        this.messageReactionRepository.deleteById(messageReactionId);
        return null;
    }
}
