package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.dto.request.CommentRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/get-all-by-music")
    public ApiResponse<List<CommentResponse>> getComments(@RequestParam(value = "id") String musicId) {
        List<CommentResponse> commentResponse = commentService.findByMusic(BigInteger.valueOf(Long.parseLong(musicId)));
        return ApiResponse.<List<CommentResponse>>builder().result(commentResponse).build();
    }

    @PostMapping("/insert")
    public ApiResponse<CommentResponse> insert(
            @RequestBody CommentRequest request
    ) {
        return ApiResponse.<CommentResponse>builder().result(commentService.insert(request)).build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Boolean> delete(
            @RequestParam(value = "id") String commentId
    ) {
        return ApiResponse.<Boolean>builder().result(commentService.delete(BigInteger.valueOf(Long.parseLong(commentId)))).build();
    }

    @PatchMapping("/update")
    public ApiResponse<CommentResponse> update(
            @RequestBody CommentRequest commentRequest) {
        CommentResponse userResponse = commentService.update(commentRequest);
        return ApiResponse.<CommentResponse>builder().result(userResponse).build();
    }

}
