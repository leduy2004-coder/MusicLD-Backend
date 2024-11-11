package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.service.HeartService;
import com.myweb.MusicLD.service.MusicService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/music")
public class MusicController {

    private final MusicService musicService;
    private final HeartService heartService;

    @GetMapping("/get-music")
    public ApiResponse<MusicResponse> getMusic(@RequestParam(value = "id") String id) {
         MusicResponse musicResponse = musicService.findMusicById(BigInteger.valueOf(Long.parseLong(id)));
        return ApiResponse.<MusicResponse>builder().result(musicResponse).build();
    }
    @PostMapping("/upload")
    public ApiResponse<MusicResponse> uploadMusic(
            @RequestParam("songName") String songName,
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam("file") MultipartFile file,
            @RequestParam("lyrics") String lyrics,
            @RequestParam("access") String access) throws UnsupportedAudioFileException, IOException {
        MusicRequest request = new MusicRequest(null, songName, lyrics, avatar, file, null, access);
        return ApiResponse.<MusicResponse>builder().result(musicService.uploadMusic(request)).build();
    }

    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteImage(@RequestBody MusicRequest.MusicRequestDelete request) {
        return ApiResponse.<Boolean>builder().result(musicService.deleteMusic(request.getPublicIdMusic(), request.getPublicIdAvatar(), request.getId())).build();
    }

    @GetMapping("/get-playlist")
    public ApiResponse<List<MusicResponse>> findAllRequest(@RequestParam(value = "id") BigInteger id) {
        List<MusicResponse> list = musicService.findByStatus(id, true,null);
        return ApiResponse.<List<MusicResponse>>builder().result(list).build();
    }
    @GetMapping("/get-playlist-access")
    public ApiResponse<List<MusicResponse>> findAllByAccess(@RequestParam(value = "id") BigInteger id, @RequestParam(value = "access") String access) {
        List<MusicResponse> list = musicService.findByStatus(id, true,access );
        return ApiResponse.<List<MusicResponse>>builder().result(list).build();
    }
    @GetMapping("/get-count")
    public ApiResponse<Long> getCountMusic(@RequestParam(value = "userId") BigInteger userId) {
        long count = musicService.countMusic(userId);
        return ApiResponse.<Long>builder().result(count).build();
    }
    @PatchMapping("/update-music")
    public ApiResponse<MusicResponse> updateMusic(
            @RequestParam("id") BigInteger id,
            @RequestParam("title") String title,
            @RequestParam("lyrics") String lyrics,
            @RequestParam(value = "fileAvatar", required = false) MultipartFile fileAvatar,
            @RequestParam(value = "publicIdAvatar", required = false) String publicIdAvatar,
            @RequestParam("access") String access) {
        MusicRequest musicRequest = new MusicRequest(id, title, lyrics, fileAvatar, null, publicIdAvatar, access);
        MusicResponse musicResponse = musicService.updateById(musicRequest);
        return ApiResponse.<MusicResponse>builder().result(musicResponse).build();

    }

    @PostMapping("/like")
    public ApiResponse<Boolean> like(@RequestParam(value = "userId") String userId,
                                            @RequestParam(value = "musicId") BigInteger musicId) {
        return ApiResponse.<Boolean>builder().result(heartService.likeMusic(BigInteger.valueOf(Long.parseLong(userId)),musicId)).build();
    }
    @PostMapping("/un-like")
    public ApiResponse<Boolean> unLike(@RequestParam(value = "userId") String userId,
                                     @RequestParam(value = "musicId") BigInteger musicId) {
        return ApiResponse.<Boolean>builder().result(heartService.unLikeMusic(BigInteger.valueOf(Long.parseLong(userId)),musicId)).build();
    }
}
