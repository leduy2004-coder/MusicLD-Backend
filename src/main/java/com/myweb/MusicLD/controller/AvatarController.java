package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.service.AvatarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@AllArgsConstructor
@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    @PostMapping("/upload")
    public ApiResponse<AvatarResponse> uploadImage(@RequestParam("image")MultipartFile file) {
        AvatarResponse avatarResponse = avatarService.uploadImage(file);
        return ApiResponse.<AvatarResponse>builder().result(avatarResponse).build();
    }

//    @GetMapping("/{fileName}")
//    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
//        byte[] imageData = avatarService.downloadImage(fileName);
//        return ResponseEntity.status(HttpStatus.OK)
//                .contentType(MediaType.valueOf("image/png"))
//                .body(imageData);
//    }

    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteImage(@RequestParam("publicId") String publicId) {
        return ApiResponse.<Boolean>builder().result(avatarService.deleteImage(publicId)).build();
    }
}
