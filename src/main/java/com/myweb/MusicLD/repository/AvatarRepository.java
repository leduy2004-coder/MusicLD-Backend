package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<AvatarEntity,BigInteger>{

    @Query("SELECT a from AvatarEntity a WHERE a.status = ?2 and a.userEntity.id=?1")
    List<AvatarEntity> findByStatusAndUser(BigInteger id,Boolean status);
}
