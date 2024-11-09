package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.HeartEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.jpa.HeartRepository;
import com.myweb.MusicLD.repository.jpa.MusicRepository;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.service.HeartService;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HeartImpl implements HeartService {
    private final AvatarService avatarService;
    private final MusicRepository musicRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final HeartRepository heartRepository;


    @Override
    public boolean likeMusic(BigInteger userId, BigInteger musicId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        MusicEntity music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        if (checkLike(userId, musicId)) {
            return false;
        }
        heartRepository.save(HeartEntity.builder().musicEntity(music).userEntity(user).build());
        return true;
    }

    @Override
    public boolean unLikeMusic(BigInteger userId, BigInteger musicId) {
        Optional<HeartEntity> like = heartRepository.findByUserIdAndMusicId(userId, musicId);
        if (like.isPresent()) {
            heartRepository.delete(like.get());
            return true;
        }
        return false;
    }

    @Override
    public List<UserResponse> findAllByMusic(BigInteger musicId) {
        List<HeartEntity> likes = heartRepository.findByMusicId(musicId);
        return likes.stream()
                .map(like -> mapper.map(like.getUserEntity(), UserResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public long countLike(BigInteger musicId) {
        return heartRepository.countByMusicId(musicId);
    }

    @Override
    public boolean checkLike(BigInteger userId, BigInteger musicId) {
        return heartRepository.existsByUserIdAndMusicId(userId, musicId);
    }
}
