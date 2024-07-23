package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.TokenResponse;
import com.myweb.MusicLD.entity.TokenEntity;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.TokenRepository;
import com.myweb.MusicLD.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    @Override
    @Transactional(readOnly = true)
    public List<TokenResponse> findAllValidTokenByUser(Long id) {
        return tokenRepository.findAllValidTokenByUser(id).stream()
                .map(token -> modelMapper.map(token,TokenResponse.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse findByToken(String token) {
        TokenEntity storedToken = tokenRepository.findByToken(token).orElse(null);
        if(storedToken == null) return null;
        return modelMapper.map(storedToken,TokenResponse.class);
    }

    @Override
    public TokenResponse save(TokenRequest token) {
        TokenEntity storedToken = tokenRepository.save(modelMapper.map(token, TokenEntity.class));
        if(token.getId() != null) {
             return modelMapper.map(token,TokenResponse.class);
        }
        return modelMapper.map(storedToken,TokenResponse.class);
    }
}
