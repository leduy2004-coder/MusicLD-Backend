package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.service.AvatarService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    @PostMapping
    public ApiResponse<?> uploadImage(@RequestParam("image")MultipartFile file) throws IOException {
        String uploadImage = avatarService.uploadImage(file);
        return ApiResponse.builder().result(uploadImage).build();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] imageData = avatarService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }

}
