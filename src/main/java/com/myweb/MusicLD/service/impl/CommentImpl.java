package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.CommentRequest;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.CommentEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.jpa.CommentRepository;
import com.myweb.MusicLD.repository.jpa.MusicRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CommentService;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MusicRepository musicRepository;
    private final MusicService musicService;
    private final ModelMapper modelMapper;
    private final AvatarService avatarService;

    @Override
    public List<CommentResponse> findByMusic(BigInteger musicId) {
        MusicEntity music = musicRepository.findById(musicId)
                .orElse(null);
        if (music == null) {
            throw new AppException(ErrorCode.MUSIC_NOT_EXISTED);
        }
        List<CommentEntity> list = commentRepository.findByMusicEntity(music);
        return list.stream()
                .map(this::getCommentResponse)
                .toList();
    }

    @Override
    public CommentResponse insert(CommentRequest commentRequest) {
        CommentEntity entity = modelMapper.map(commentRequest, CommentEntity.class);
        entity.setUserEntity(GetInfo.getLoggedInUserInfo());
        MusicEntity music = musicRepository.findById(commentRequest.getMusicId()).orElse(null);
        entity.setMusicEntity(music);
        if (commentRequest.getParentId() != null) {
            CommentEntity parent = commentRepository.findById(commentRequest.getParentId()).orElse(null);
            entity.setParentComment(parent);
        }
        entity = commentRepository.save(entity);
        return getCommentResponse(entity);
    }

    private CommentResponse getCommentResponse(CommentEntity entity) {
        UserResponse userResponse = modelMapper.map(entity.getUserEntity(), UserResponse.class);
        userResponse.setAvatar(avatarService.findByStatus(userResponse.getId(), true, AvatarType.USER));
        return CommentResponse.builder()
                .id(entity.getId())
                .createdDate(entity.getCreatedDate())
                .content(entity.getContent())
                .parentId(entity.getParentComment() != null ? entity.getParentComment().getId() : null)
                .userResponse(userResponse)
                .build();
    }

    @Override
    public CommentResponse update(CommentRequest commentRequest) {
        CommentEntity comment = commentRepository.findById(commentRequest.getId())
                .orElse(null);
        if (comment == null) {
            throw new AppException(ErrorCode.COMMENT_NOT_EXISTED);
        }
        comment.setContent(commentRequest.getContent());
        CommentEntity commentEntity = commentRepository.save(comment);
        return getCommentResponse(commentEntity);
    }

    @Override
    public Boolean delete(BigInteger id) {
        try {
            commentRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

}
