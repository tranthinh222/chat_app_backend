package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.repository.ConversationRepository;
import com.thinhtran.chatapp.service.ConversationService;
import com.thinhtran.chatapp.service.UploadService;
import com.thinhtran.chatapp.service.UserService;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
import com.thinhtran.chatapp.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class UploadController {
    private final UploadService uploadService;
    private final UserService userService;
    private final ConversationService conversationService;
    public UploadController(UploadService uploadService,  UserService userService,  ConversationService conversationService) {
        this.uploadService = uploadService;
        this.userService = userService;
        this.conversationService = conversationService;
    }

    @PostMapping("/upload/users/{id}")
    @ApiMessage("upload image")
    public ResponseEntity<?> uploadUserImage(@RequestParam("file") MultipartFile file,
                                         @PathVariable Long id) throws IOException {
        User user = this.userService.getUserById(id);
        if (user == null)
        {
            throw new IdInvalidException("user id with " + id + " not found");
        }

        String url = this.uploadService.uploadUserAvatar(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("url", url));
    }

    @PostMapping("/upload/groups/{id}")
    @ApiMessage("upload group image")
    public ResponseEntity<?> uploadConversationImage(@RequestParam("file") MultipartFile file,
                                             @PathVariable Long id) throws IOException {
        Conversation conversation = this.conversationService.getConversationById(id);
        if (conversation == null){
            throw new IdInvalidException("conversation id with " + id + " not found");
        }
        String url = this.uploadService.uploadGroupAvatar(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("url", url));
    }


}
