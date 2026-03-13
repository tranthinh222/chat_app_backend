package com.thinhtran.chatapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinhtran.chatapp.util.SecurityUtil;
import com.thinhtran.chatapp.util.constant.RoomTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoomTypeEnum roomType;
    private String avatar;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name="created_by")
    private User createdBy;

    @PrePersist
    public void handleBeforeCreated() {
        this.createdAt = Instant.now();
    }
}
