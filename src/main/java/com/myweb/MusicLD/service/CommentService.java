package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.CommentRequest;
import com.myweb.MusicLD.dto.response.CommentResponse;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {
    List<CommentResponse> findByMusic(BigInteger id);
    CommentResponse insert(CommentRequest commentRequest);
    CommentResponse update(CommentRequest commentRequest);
    Boolean delete(BigInteger id);

}
