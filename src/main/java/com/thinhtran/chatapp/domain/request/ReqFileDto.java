package com.thinhtran.chatapp.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqFileDto {
    private String fileUrl;
    private String fileName;
    private String fileType;
    private int fileSize;
}
