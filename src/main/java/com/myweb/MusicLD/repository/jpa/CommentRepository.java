package com.myweb.MusicLD.repository.jpa;

import com.myweb.MusicLD.entity.CommentEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, BigInteger> {
    List<CommentEntity> findByMusicEntity (MusicEntity musicEntity);
    List<CommentEntity> findByParentCommentIsNull();
    List<CommentEntity> findByParentComment(CommentEntity parentComment);
}
