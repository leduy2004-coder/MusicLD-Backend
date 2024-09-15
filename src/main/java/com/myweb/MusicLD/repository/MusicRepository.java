package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, BigInteger> {

    @Query("SELECT a from MusicEntity a WHERE a.status = ?2 and a.userEntity.id=?1")
    List<MusicEntity> findByStatusAndMusic(BigInteger id, Boolean status);

    @Query("SELECT a from MusicEntity a WHERE a.status = ?2 and a.userEntity.id=?1 and a.access = ?3")
    List<MusicEntity> findByStatusAndMusicAndAccess(BigInteger id, Boolean status, AccessMusic accessMusic);

    @Modifying
    @Transactional
    @Query("UPDATE MusicEntity u SET u.status = :status WHERE u.id = :id")
    void updateStatus(@Param("id") BigInteger id, @Param("status") Boolean status);
}
