package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long>, JpaSpecificationExecutor<ConversationMember> {

}
