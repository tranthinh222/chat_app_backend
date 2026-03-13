package com.thinhtran.chatapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.RoleMemberEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.CloseableThreadContext;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversation_members")
public class ConversationMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="conversation_id")
    private Conversation conversation;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="user_id")
    private User user;
    private RoleMemberEnum role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant joinedAt;

    @PrePersist
    public void handleBeforeCreated() {
        this.joinedAt = Instant.now();
    }
}
