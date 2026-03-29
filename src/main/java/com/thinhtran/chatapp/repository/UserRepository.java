package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    User findByResetToken(String resetToken);

    User findUserById(Long id);
    User findUserByPhone(String phone_number);

//    @Query("""
//            SELECT m.user
//            FROM Conversation c
//            JOIN Member m ON c.id = m.conversation.id
//            WHERE c.roomType = "DIRECT"
//                    AND c.id = :conversationId
//                    AND m.user.id <> :userId
//        """)
//    User findOtherUserByConversationId(Long conversationId, Long userId);
}
