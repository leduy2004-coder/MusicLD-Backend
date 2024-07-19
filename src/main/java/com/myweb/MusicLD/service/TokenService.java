package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.response.TokenResponse;

import java.util.List;

public interface TokenService {
    List<TokenResponse> findAllValidTokenByUser(Long id);


    TokenResponse findByToken(String token);
    TokenResponse save(TokenRequest token);
}
