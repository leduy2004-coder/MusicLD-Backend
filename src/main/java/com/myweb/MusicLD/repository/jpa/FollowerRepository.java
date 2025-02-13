package com.myweb.MusicLD.repository.jpa;

import com.myweb.MusicLD.entity.FollowerEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<FollowerEntity, BigInteger> {
    FollowerEntity findBySenderAndReceiver(UserEntity sender, UserEntity receiver);

    @Modifying
    @Transactional
    @Query("UPDATE FollowerEntity f SET f.status = :status WHERE f.sender.id = :idSender AND f.receiver.id = :idReceiver")
    void updateStatus(@Param("idSender") BigInteger idSender, @Param("idReceiver") BigInteger idReceiver, @Param("status") RequestFollowStatus status);

    @Query("SELECT a.receiver FROM FollowerEntity a WHERE a.status = ?2 AND a.sender.id = ?1")
    List<UserEntity> findAllReceivers(BigInteger senderId, RequestFollowStatus status);

    @Query("SELECT a.sender from FollowerEntity a WHERE a.status = ?2 and a.receiver.id=?1")
    List<UserEntity> findAllSenders(BigInteger id, RequestFollowStatus status);

    @Query("SELECT COUNT(f) FROM FollowerEntity f WHERE f.receiver.id = :id AND f.status = :status")
    long countFollowersByReceiverId(@Param("id") BigInteger receiverId, @Param("status") RequestFollowStatus status);
}
