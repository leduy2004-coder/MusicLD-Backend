package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Query(value = """
      select t from TokenEntity t inner join UserEntity u\s
      on t.userEntity.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<TokenEntity> findAllValidTokenByUser(Long id);

    @Query(value = "SELECT t FROM TokenEntity t JOIN FETCH t.userEntity u WHERE t.token = :token")
    Optional<TokenEntity> findByToken(@Param("token") String token);
}