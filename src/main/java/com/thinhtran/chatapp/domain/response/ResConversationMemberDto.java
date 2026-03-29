package com.thinhtran.chatapp.domain.response;

import com.thinhtran.chatapp.util.constant.RoleMemberEnum;

public interface ResConversationMemberDto {
    Long getUserId();
    String getUserName();
    String getAvatar();
    RoleMemberEnum getRoleMember();
}
