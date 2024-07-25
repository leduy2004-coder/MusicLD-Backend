package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.AvatarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<AvatarEntity,Long> {

    Optional<AvatarEntity> findByName(String fileName);

    List<AvatarEntity> findByStatus(Boolean status);
}
