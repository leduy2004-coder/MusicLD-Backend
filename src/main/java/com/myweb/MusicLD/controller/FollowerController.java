package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.service.FollowerService;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follow")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FollowerController {

    FollowerService followerService;


    @GetMapping("/get-all-follower/{id}")
    public ApiResponse<List<UserResponse>> findAllFollower(@PathVariable BigInteger id) {
        List<UserResponse> list = followerService.findAllFollowers(id);
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @GetMapping("/get-all-request/{id}")
    public ApiResponse<List<UserResponse>> findAllRequest(@PathVariable BigInteger id) {
        List<UserResponse> list = followerService.findAllRequestFollow(id);
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @GetMapping("/get-all-receive/{id}")
    public ApiResponse<List<UserResponse>> findAllReceive(@PathVariable BigInteger id) {
        List<UserResponse> list = followerService.findAllReceiverFollow(id);
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @GetMapping("/get-all-following/{id}")
    public ApiResponse<List<UserResponse>> findAllFollowing(@PathVariable BigInteger id) {
        List<UserResponse> list = followerService.findAllFollowing(id);
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @GetMapping("/get-status-following/{id}")
    public ApiResponse<RequestFollowStatus> findStatusFollowing(@PathVariable BigInteger id) {
        RequestFollowStatus list = followerService.getStatus(id);
        return ApiResponse.<RequestFollowStatus>builder().result(list).build();
    }

    @PatchMapping("/{id}/{status}")
    public ApiResponse<RequestFollowStatus> updateRequestFollow(@PathVariable BigInteger id, @PathVariable String status) {
        RequestFollowStatus statusUpdate = followerService.updateRequestFollow(id, RequestFollowStatus.valueOf(status));
        return ApiResponse.<RequestFollowStatus>builder().result(statusUpdate).build();
    }

    @PostMapping("/check-follow/{id}")
    public ApiResponse<Boolean> deleteImage(@PathVariable BigInteger id) {
        return ApiResponse.<Boolean>builder().result(followerService.checkFollow(id)).build();
    }
}