package com.thinhtran.chatapp.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageFileDto {
    private String fileUrl;
    private String fileName;
    private String fileType;
    private int fileSize;
}
