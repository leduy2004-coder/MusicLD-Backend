package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;

import java.math.BigInteger;
import java.util.List;

public interface FollowerService {
    RequestFollowStatus updateRequestFollow(BigInteger followedId, RequestFollowStatus status);
    RequestFollowStatus getFollowStatus(UserEntity follower, UserEntity followed);
    List<UserResponse> findAllFollowing(BigInteger id);
    List<UserResponse> findAllFollowers(BigInteger id);
    List<UserResponse> findAllRequestFollow(BigInteger id);
    List<UserResponse> findAllReceiverFollow(BigInteger id);
}
