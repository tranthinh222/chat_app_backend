package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.MessageReaction;
import com.thinhtran.chatapp.domain.MessageReactionId;
import com.thinhtran.chatapp.domain.request.ReqCreateMessageReactionDto;
import com.thinhtran.chatapp.domain.request.ReqUpdateMessageReactionDto;
import com.thinhtran.chatapp.domain.response.ResMessageReactionDto;
import com.thinhtran.chatapp.service.MessageReactionService;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
import com.thinhtran.chatapp.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class MessageReactionController {
    private final MessageReactionService messageReactionService;
    public MessageReactionController(MessageReactionService messageReactionService) {
        this.messageReactionService = messageReactionService;
    }


    @GetMapping("/reactions/{id}")
    @ApiMessage("fetch reaction by message id")
    public ResponseEntity<List<ResMessageReactionDto>> getReactionByMessageId(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.messageReactionService.getMessageReactionByMessageId(id));
    }

    @PostMapping("/reactions")
    @ApiMessage("create a message creation")
    public ResponseEntity<MessageReaction> createMessageCreation(@Valid @RequestBody ReqCreateMessageReactionDto reqMessageCreation) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.messageReactionService.createMessageReaction(reqMessageCreation));

    }

    @PutMapping("/reactions")
    @ApiMessage("update a message reaction")
    public ResponseEntity<MessageReaction> updateMessageCreation(@Valid @RequestBody ReqUpdateMessageReactionDto  reqMessageCreation)
    {
        Optional<MessageReaction> messageReaction = this.messageReactionService.getMessageReactionById(reqMessageCreation.getId());

        if (!messageReaction.isPresent()) {
            throw new IdInvalidException("Message reaction not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.messageReactionService.updateMessageReaction(reqMessageCreation));
    }

    @DeleteMapping("/reactions")
    @ApiMessage("delete a message reaction")
    public ResponseEntity<Void> deleteMessageCreation(MessageReactionId messageReactionId) {
        Optional<MessageReaction> messageReaction = this.messageReactionService.getMessageReactionById(messageReactionId);

        if (!messageReaction.isPresent()) {
            throw new IdInvalidException("Message reaction not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.messageReactionService.deleteMessageReaction(messageReactionId));
    }
}
