package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.MusicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity,BigInteger>{

    @Query("SELECT a from MusicEntity a WHERE a.status = ?2 and a.userEntity.id=?1")
    List<MusicEntity> findByStatusAndMusic(BigInteger id,Boolean status);
}
