package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.entity.UserEntity;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {
    List<CommentResponse> findByMusic(BigInteger id);
    CommentResponse insert(CommentReq userDto);

}
