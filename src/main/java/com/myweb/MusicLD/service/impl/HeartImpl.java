package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.HeartEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.jpa.HeartRepository;
import com.myweb.MusicLD.repository.jpa.MusicRepository;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.HeartService;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> findAllByMusic(BigInteger musicId) {
        List<HeartEntity> likes = heartRepository.findByMusicId(musicId);
        return likes.stream()
                .map(like -> {
                    UserResponse userResponse = mapper.map(like.getUserEntity(), UserResponse.class);
                    userResponse.setAvatar(avatarService.findByStatus(userResponse.getId(), true, AvatarType.USER));
                    return userResponse;
                })
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

    @Override
    public Map<BigInteger, Long> countLikesForMusicIds(List<BigInteger> musicIds) {
        List<Object[]> results = heartRepository.countLikesForMusicIds(musicIds);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (BigInteger) row[0],  // id
                        row -> (Long) row[1]         // count
                ));
    }

    @Override
    public Map<BigInteger, Boolean> checkLikesForUser(BigInteger userId, List<BigInteger> musicIds) {
        List<BigInteger> likedMusicIds = heartRepository.findLikedMusicIdsForUser(userId, musicIds);

        // Kiểm tra sự trùng lặp hoặc lỗi
        Set<BigInteger> uniqueLikedMusicIds = new HashSet<>(likedMusicIds); // Loại bỏ trùng lặp

        // Xây dựng Map với trạng thái 'liked' (true/false) cho từng bài hát
        return musicIds.stream()
                .collect(Collectors.toMap(
                        musicId -> musicId,
                        uniqueLikedMusicIds::contains
                ));
    }
}
