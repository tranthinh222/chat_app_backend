package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.ReactionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResMessageReactionDetailDto {
    private Long messageId;
    private Long userId;
    private ReactionEnum reactionType;
}
