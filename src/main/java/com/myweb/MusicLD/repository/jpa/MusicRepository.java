package com.myweb.MusicLD.repository.jpa;

import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT COUNT (m) from MusicEntity m where m.userEntity.id = ?2 and m.status = ?1")
    long countMusic(Boolean status, BigInteger id);


    @Query("SELECT u FROM MusicEntity u " +
            "JOIN HeartEntity f ON f.musicEntity.id = u.id " +
            "GROUP BY u " +
            "ORDER BY COUNT(f) DESC")
    List<MusicEntity> getTopMusicsByHeart(Pageable pageable);

    @Query(value = "EXEC dbo.spStatisticsMusicByYear :year", nativeQuery = true)
    List<Tuple> getCountMusicsByYear(@Param("year") int year);


    @Query(value = "SELECT * FROM dbo.fnStatisticsByYear(:year)", nativeQuery = true)
    Tuple getStatisticByYear(@Param("year") int year);

}
