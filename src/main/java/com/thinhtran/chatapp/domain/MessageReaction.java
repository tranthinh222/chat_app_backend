package com.thinhtran.chatapp.domain;

import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.ReactionEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message_reactions")
public class MessageReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="message_id")
    private Message message;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="user_id")
    private User user;

    private ReactionEnum reactionType;
    private Instant createdAt;
    @PrePersist
    public void handleBeforeCreated() {
        this.createdAt = Instant.now();
    }
}
