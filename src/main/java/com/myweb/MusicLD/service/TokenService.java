package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.TokenDTO;

import java.util.List;

public interface TokenService {
    List<TokenDTO> findAllValidTokenByUser(Long id);


    TokenDTO findByToken(String token);
    TokenDTO save(TokenDTO token);
}
