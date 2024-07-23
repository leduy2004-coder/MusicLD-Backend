package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.TokenResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.TokenEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.TokenRepository;
import com.myweb.MusicLD.service.TokenService;
import com.myweb.MusicLD.utility.GetInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        TokenResponse storedToken = tokenService.findByToken(jwt);
        if (storedToken.getId() != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(modelMapper.map(storedToken, TokenEntity.class));
            SecurityContextHolder.clearContext();
        }
    }
}