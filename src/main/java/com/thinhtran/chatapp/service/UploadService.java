package com.thinhtran.chatapp.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.thinhtran.chatapp.domain.User;
import com.thinhtran.chatapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {
    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    public String uploadImage (MultipartFile file, Long id) throws IOException {
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

}
