package com.thinhtran.chatapp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thinhtran.chatapp.domain.Conversation;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.repository.ConversationRepository;
import com.thinhtran.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    public String uploadUserAvatar (MultipartFile file, Long id) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "uploads",
                        "resource_type", "image"
                ));

        User user = userRepository.findUserById(id);
        user.setAvatar(uploadResult.get("url").toString());
        this.userRepository.save(user);

        return uploadResult.get("secure_url").toString();
    }

    public String uploadGroupAvatar (MultipartFile file, Long conversationId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "uploads",
                        "resource_type", "image"
                ));

        Optional<Conversation> conversation =  conversationRepository.findById(conversationId);
        conversation.get().setAvatar(uploadResult.get("url").toString());
        this.conversationRepository.save(conversation.get());

        return uploadResult.get("secure_url").toString();
    }
}
