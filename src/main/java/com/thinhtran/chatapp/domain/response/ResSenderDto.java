package com.thinhtran.chatapp.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResSenderDto {
    private Long userId;
    private String username;
    private String avatar;
}
