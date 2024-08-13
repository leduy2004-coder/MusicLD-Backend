package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, BigInteger> {
     Optional<RoleEntity> findByCode(String code);
}
