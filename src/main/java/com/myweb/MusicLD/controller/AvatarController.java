package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/upload")
    public ApiResponse<AvatarResponse> uploadImage(@RequestParam("image")MultipartFile file) {
        AvatarResponse avatarResponse = avatarService.uploadImage(file, AvatarType.USER, MusicEntity.builder().build());
        return ApiResponse.<AvatarResponse>builder().result(avatarResponse).build();
    }

    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteImage(@RequestParam("publicId") String publicId) {
        BigInteger userId = Objects.requireNonNull(GetInfo.getLoggedInUserInfo()).getId();
        return ApiResponse.<Boolean>builder().result(avatarService.deleteImage(publicId, AvatarType.USER,userId)).build();
    }
}
