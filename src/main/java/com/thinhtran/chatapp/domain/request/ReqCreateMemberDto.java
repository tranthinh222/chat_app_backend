package com.thinhtran.chatapp.domain.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqCreateMemberDto {
    @NotNull
    private Long conversationId;
    @NotNull
    private Long userId;
}
