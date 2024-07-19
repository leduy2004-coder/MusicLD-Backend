package com.myweb.MusicLD.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.AuthenticationRequest;
import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.TokenResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.service.TokenService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private CustomUserDetails customUserDetails;
    private final ModelMapper modelMapper;


    public AuthenticationResponse register(UserRequest request) {
        UserEntity userSaver = new UserEntity();
        try {
            customUserDetails = new CustomUserDetails();
            UserResponse userResponse = userService.insert(request);
             userSaver =modelMapper.map(userResponse, UserEntity.class);
            customUserDetails.setUser(userSaver);
            var jwtToken = jwtService.generateToken(customUserDetails);
            var refreshToken = jwtService.generateRefreshToken(customUserDetails);
            saveUserToken(userSaver, jwtToken);
            return AuthenticationResponse.builder()
                    .userResponse(userResponse)
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }catch (Exception e) {
            System.out.println(userSaver);
            e.printStackTrace();
        }
        return null;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var jwtToken = "";
        var refreshToken = "";
        UserResponse userDTO = new UserResponse();
        try {
             authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            customUserDetails = new CustomUserDetails();
            userDTO = userService.findByUsername(request.getUsername());
            UserEntity user = modelMapper.map(userDTO, UserEntity.class);

            customUserDetails.setUser(user);
            jwtToken = jwtService.generateToken(customUserDetails);
            refreshToken = jwtService.generateRefreshToken(customUserDetails);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

        }catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userResponse(userDTO)
                .build();
    }
    private void revokeAllUserTokens(UserEntity user) {
        List<TokenResponse> validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenService.save(modelMapper.map(token, TokenRequest.class));
        });

    }

    public void saveUserToken(UserEntity user, String jwtToken) {
        TokenRequest token = TokenRequest.builder()
                .userEntity(modelMapper.map(user,UserResponse.class))
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(token);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userName;
        if (authHeader == null ||!authHeader.startsWith("Bearer")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userName = jwtService.extractUserName(refreshToken);
        if (userName != null) {
            customUserDetails = new CustomUserDetails();
            UserEntity user =  modelMapper.map(userService.findByUsername(userName),UserEntity.class);
            customUserDetails.setUser(user);
            if (jwtService.isTokenValid(refreshToken, customUserDetails)) {
                var accessToken = jwtService.generateToken(customUserDetails);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}
