package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.MessageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageFileRepository extends JpaRepository<MessageFile, Long> {
    @Query("""
          SELECT f
          FROM MessageFile f
          WHERE f.message.id IN :messageIds
     """)
    List<MessageFile> findAllByMessageIds(List<Long> messageIds);
    MessageFile findTopByMessageId(Long messageId);
    MessageFile findByMessageId(Long messageId);
}
