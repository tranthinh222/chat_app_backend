package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.MessageReaction;
import com.thinhtran.chatapp.domain.MessageReactionId;
import com.thinhtran.chatapp.domain.response.ResMessageReactionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageReactionRepository extends JpaRepository<MessageReaction, MessageReactionId>, JpaSpecificationExecutor<MessageReaction> {

    @Query("""
        SELECT 
            m.id AS messageReactionId,
            u.username AS userName,
            u.avatar AS avatar,
            m.reactionType AS reactionType
        FROM MessageReaction m
        JOIN m.user u
        WHERE m.message.id = :messageId
    """)
    List<ResMessageReactionDto> findAllByMessageId(Long messageId);

    @Query("""
        SELECT 
            u.id AS userId,
            u.username AS userName,
            u.avatar AS avatar,
            m.reactionType AS reactionType
        FROM MessageReaction m
        JOIN m.user u
        WHERE m.message.id = :id
    """)
    List<ResMessageReactionDto> findAllByMessageIds(List<Long> messageId);
}
