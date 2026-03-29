package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.domain.MessageReactionId;
import com.thinhtran.chatapp.util.constant.ReactionEnum;


public interface ResMessageReactionDto {
    MessageReactionId getMessageReactionId();
    String getUsername();
    String getAvatar();
    ReactionEnum getReactionType();

}
