package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;

import java.math.BigInteger;
import java.util.List;

public interface HeartService {
    boolean likeMusic(BigInteger userId, BigInteger musicId);
    boolean unLikeMusic(BigInteger userId, BigInteger musicId);
    List<UserResponse> findAllByMusic(BigInteger musicId);
    long countLike(BigInteger musicId);
    boolean checkLike(BigInteger userId, BigInteger musicId);
}
