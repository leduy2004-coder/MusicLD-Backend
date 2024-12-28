package com.myweb.MusicLD.repository.jpa;

import com.myweb.MusicLD.entity.HeartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<HeartEntity, BigInteger> {
    @Query("SELECT h FROM HeartEntity h where h.musicEntity.id = ?2 and  h.userEntity.id = ?1")
    Optional<HeartEntity> findByUserIdAndMusicId(BigInteger userId, BigInteger musicId);

    @Query("SELECT h from  HeartEntity h where h.musicEntity.id = ?1")
    List<HeartEntity> findByMusicId(BigInteger musicId);

    @Query("SELECT count(h) from  HeartEntity h where h.musicEntity.id = ?1")
    long countByMusicId(BigInteger musicId);

    @Query("SELECT COUNT(h) > 0 FROM HeartEntity h WHERE h.userEntity.id = ?1 AND h.musicEntity.id = ?2")
    boolean existsByUserIdAndMusicId(BigInteger userId, BigInteger musicId);
}