package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.Member;
import com.thinhtran.chatapp.domain.response.ResConversationMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

    @Query("""
        SELECT 
            u.id AS userId,
            u.username AS userName,
            u.avatar AS avatar,
            m.role AS roleMember
        FROM Member m
        JOIN m.user u
        WHERE m.conversation.id = :id
    """)
    List<ResConversationMemberDto> findMemberByConversationId(Long id);
    List<Member> findByConversationId(Long conversationId);
    List<Long> findUserIdsByConversationId(Long conversationId);
}
