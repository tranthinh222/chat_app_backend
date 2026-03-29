package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>, JpaSpecificationExecutor<Conversation> {
    @Query("""
        SELECT c
        FROM Conversation c
        JOIN Member m ON c.id = m.conversation.id
        WHERE m.user.id = :userId
    """)
    List<Conversation> findByUserId(Long userId);



}
