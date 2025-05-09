package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@AllArgsConstructor
@RestController
@RequestMapping("/api/avatar")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarController {

    AvatarService avatarService;
    UserService userService;

    @PostMapping("/upload")
    public ApiResponse<AvatarResponse> uploadImage(@RequestParam("image")MultipartFile file) {
        AvatarResponse avatarResponse = avatarService.uploadImage(file, AvatarType.USER, MusicEntity.builder().build());
        return ApiResponse.<AvatarResponse>builder().result(avatarResponse).build();
    }
    @PostMapping("/upload-user")
    public ApiResponse<AvatarResponse> uploadImageUser(@RequestParam("image")MultipartFile file, @RequestParam("userId")BigInteger userId) {
        AvatarResponse avatarResponse = avatarService.uploadImageUser(file,  userId);
        return ApiResponse.<AvatarResponse>builder().result(avatarResponse).build();
    }
    @PostMapping("/update-status")
    public ApiResponse<Boolean> updateStatus(@RequestParam(value = "publicId", required = false) String publicId) {
        UserResponse userResponse = userService.findByUsername(GetInfo.getLoggedInUserName());
        BigInteger userId = userResponse.getId();
        avatarService.updatedAvatars(AvatarType.USER,userId);
        return ApiResponse.<Boolean>builder().result(true).build();
    }
}
