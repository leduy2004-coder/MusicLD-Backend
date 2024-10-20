package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.FollowerEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.jpa.FollowerRepository;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.FollowerService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowerImpl implements FollowerService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final ModelMapper modelMapper;
    private final AvatarService avatarService;

    @Override
    @Transactional
    public RequestFollowStatus updateRequestFollow(BigInteger followedId, RequestFollowStatus status) {
        UserEntity follower = GetInfo.getLoggedInUserInfo();
        UserEntity followed = userRepository.findById(followedId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (getFollowStatus(follower,followed) == null && status != RequestFollowStatus.ACCEPTED) {
            followerRepository.save(FollowerEntity.builder()
                    .sender(follower)
                    .receiver(followed)
                    .status(RequestFollowStatus.PENDING)
                    .build());
            return status;
        } else if (status == RequestFollowStatus.PENDING) {
            followerRepository.updateStatus(Objects.requireNonNull(follower).getId(), followedId, status);
            return status;
        }else if (status == RequestFollowStatus.CANCELED) {
            if(getFollowStatus(follower,followed) == RequestFollowStatus.PENDING || getFollowStatus(follower,followed) == RequestFollowStatus.ACCEPTED) {
                followerRepository.updateStatus(Objects.requireNonNull(follower).getId(), followedId, status);
                return status;
            }
        }
        followerRepository.updateStatus(followedId, Objects.requireNonNull(follower).getId(), status);
        return status;
    }

    @Override
    public RequestFollowStatus getFollowStatus(UserEntity follower, UserEntity followed) {
        FollowerEntity followRequestEntity = followerRepository.findBySenderAndReceiver(follower, followed);
        if (followRequestEntity == null) {
            return null;
        }
        return followRequestEntity.getStatus();

    }

    @Override
    public List<UserResponse> findAllFollowing(BigInteger id) {
        return findUsersByFollowStatus(id, RequestFollowStatus.ACCEPTED, true);
    }

    @Override
    public List<UserResponse> findAllFollowers(BigInteger id) {
        return findUsersByFollowStatus(id, RequestFollowStatus.ACCEPTED, false);
    }

    @Override
    public List<UserResponse> findAllRequestFollow(BigInteger id) {
        return findUsersByFollowStatus(id, RequestFollowStatus.PENDING, true);
    }

    @Override
    public List<UserResponse> findAllReceiverFollow(BigInteger id) {
        return findUsersByFollowStatus(id, RequestFollowStatus.PENDING, false);
    }

    @Override
    public Boolean checkFollow(BigInteger id) {
        UserEntity follower = GetInfo.getLoggedInUserInfo();
        UserEntity followed = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return getFollowStatus(follower,followed) ==  RequestFollowStatus.ACCEPTED;
    }

    private List<UserResponse> findUsersByFollowStatus(BigInteger id, RequestFollowStatus status, boolean isFollower) {
        List<UserEntity> list;
        if (isFollower) {
            list = followerRepository.findAllReceivers(id, status);
        } else {
            list = followerRepository.findAllSenders(id, status);
        }

        return list.stream().map(followerEntity -> {
            UserResponse userResponse = modelMapper.map(followerEntity,
                    UserResponse.class
            );
            userResponse.setAvatar(avatarService.findByStatus(userResponse.getId(), true, AvatarType.USER));
            userResponse.setCountFollower(countFollowers(userResponse.getId()));
            return userResponse;
        }).toList();
    }
    private long countFollowers(BigInteger id) {
        return followerRepository.countFollowersByReceiverId(id, RequestFollowStatus.ACCEPTED);
    }

}
