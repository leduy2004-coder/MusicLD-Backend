package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;

    @PostMapping("/upload")
    public ApiResponse<MusicResponse> uploadMusic(
            @RequestParam("songName") String songName,
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam("file") MultipartFile file,
            @RequestParam("lyrics") String lyrics) {
        MusicRequest request = new MusicRequest(songName,lyrics, avatar, file);
        return ApiResponse.<MusicResponse>builder().result(musicService.uploadMusic(request)).build();
    }

    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteImage(@RequestBody MusicRequest.MusicRequestDelete request) {
        return ApiResponse.<Boolean>builder().result(musicService.deleteMusic(request.getPublicIdMusic(), request.getPublicIdAvatar(), request.getId())).build();
    }

    @GetMapping("/get-playlist")
    public ApiResponse<List<MusicResponse>> findAllRequest(@RequestParam(value = "id") BigInteger id){
        List<MusicResponse> list = musicService.findByStatus(id,true);
        return ApiResponse.<List<MusicResponse>>builder().result(list).build();
    }
}
