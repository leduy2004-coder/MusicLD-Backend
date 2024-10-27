package com.myweb.MusicLD.service.impl;

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
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MusicRepository musicRepository;
    private final ModelMapper modelMapper;
    private final AvatarService avatarService;

    @Override
    public List<CommentResponse> findByMusic(BigInteger musicId) {
        MusicEntity music = musicRepository.findById(musicId)
                .orElse(null);
        if (music == null) {
            throw new AppException(ErrorCode.MUSIC_EXISTED);
        }
        List<CommentEntity> list = commentRepository.findByMusicEntity(music);
        return list.stream()
                .map(commentEntity -> {
                    UserResponse userResponse = modelMapper.map(commentEntity, UserResponse.class);
                    userResponse.setAvatar(avatarService.findByStatus(commentEntity.getId(), true, AvatarType.USER));
                    return CommentResponse.builder()
                            .id(commentEntity.getId())
                            .createdDate(commentEntity.getCreatedDate())
                            .content(commentEntity.getContent())
                            .parentId(commentEntity.getParentComment() != null ? commentEntity.getParentComment().getId() : null)
                            .userResponse(userResponse).build();
                })
                .toList();
    }
}
