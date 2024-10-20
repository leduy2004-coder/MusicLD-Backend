package com.myweb.MusicLD.repository.jpa;

import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, BigInteger> {
    @Query("SELECT u from UserEntity u WHERE u.username = ?1")
    Optional<UserEntity> findByUsername(String username);

    @Query("UPDATE UserEntity u SET u.authType = ?2 WHERE u.username = ?1")
    void updateAuthenticationType(String username, AuthenticationType authType);

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nickName) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<UserEntity> searchUsers(@Param("searchString") String searchString, Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nickName) LIKE LOWER(CONCAT('%', :searchString, '%'))")
    List<UserEntity> searchFullUsers(@Param("searchString") String searchString);

    @Query("SELECT u FROM UserEntity u " +
            "JOIN u.followers f " +
            "WHERE f.status != 'CANCELED' " +
            "GROUP BY u " +
            "ORDER BY COUNT(f) DESC")
    List<UserEntity> getTopUsersByFollowers(Pageable pageable);

}
