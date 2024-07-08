package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.TokenDTO;
import com.myweb.MusicLD.entity.TokenEntity;
import com.myweb.MusicLD.repository.TokenRepository;
import com.myweb.MusicLD.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<TokenDTO> findAllValidTokenByUser(Long id) {
        return tokenRepository.findAllValidTokenByUser(id).stream()
                .map(token -> modelMapper.map(token,TokenDTO.class)).collect(Collectors.toList());
    }

    @Override
    public TokenDTO findByToken(String token) {
        TokenEntity storedToken = tokenRepository.findByToken(token).orElse(null);
        return modelMapper.map(storedToken,TokenDTO.class);
    }

    @Override
    public TokenDTO save(TokenDTO token) {
        return modelMapper.map(tokenRepository.save(modelMapper.map(token, TokenEntity.class)),TokenDTO.class);
    }
}
