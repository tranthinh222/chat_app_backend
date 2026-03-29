package com.thinhtran.chatapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.MessageTypeEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="sender_id")
    private User sender;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="conversation_id")
    private Conversation conversation;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MessageStatus> messageStatuses;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MessageReaction> messageReactions;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MessageFile> messageFiles;

    private MessageTypeEnum messageType;
    @Column(columnDefinition = "MEDIUMTEXT")
    @Nullable
    private String content;

    private Instant createdAt;
    private Instant editedAt;
    private Instant deletedAt;

    @PrePersist
    public void handleBeforeCreated() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdated() {
        this.editedAt = Instant.now();

        if (this.content == null) {
            this.deletedAt =  Instant.now();
        }

    }

}
