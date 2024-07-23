package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.AuthenticationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    @Query("SELECT u from UserEntity u WHERE u.username = ?1")
    Optional<UserEntity> findByUsername(String username);

    @Query("UPDATE UserEntity u SET u.authType = ?2 WHERE u.username = ?1")
    void updateAuthenticationType(String username, AuthenticationType authType);
}
