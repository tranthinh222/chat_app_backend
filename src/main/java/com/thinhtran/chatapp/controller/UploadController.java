package com.thinhtran.chatapp.controller;

import com.thinhtran.chatapp.service.UploadService;
import com.thinhtran.chatapp.util.annotation.ApiMessage;
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
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload/avatar/{id}")
    @ApiMessage("upload image")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file,
                                          @PathVariable Long id) throws IOException {
        String url = this.uploadService.uploadImage(file, id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("url", url));
    }
}
