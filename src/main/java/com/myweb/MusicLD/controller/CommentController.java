package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.dto.response.ApiResponse;
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

    @GetMapping("/get-comment")
    public ApiResponse<List<CommentResponse>> getComments(@RequestParam(value = "id") String musicId) {
        List<CommentResponse> commentResponse = commentService.findByMusic(BigInteger.valueOf(Long.parseLong(musicId)));
        return ApiResponse.<List<CommentResponse>>builder().result(commentResponse).build();
    }


}
