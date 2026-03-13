package com.thinhtran.chatapp.repository;

import com.thinhtran.chatapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {
    User findUserByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
    User findByResetToken(String resetToken);

    User findUserById(Long id);
}
