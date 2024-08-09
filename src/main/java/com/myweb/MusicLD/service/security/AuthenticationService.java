package com.myweb.MusicLD.service.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.AuthenticationRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.TokenRedisService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;
    private final AvatarService avatarService;
    private final AuthenticationManager authenticationManager;
    private CustomUserDetails customUserDetails= new CustomUserDetails();;
    private final ModelMapper modelMapper;
    private final TokenRedisService tokenRedisService;


    public AuthenticationResponse register(UserRequest request)  {
        UserResponse userResponse = userService.insert(request);
        UserEntity userSaver = modelMapper.map(userResponse, UserEntity.class);
        customUserDetails.setUser(userSaver);
        var jwtToken = jwtService.generateToken(customUserDetails);
        var refreshToken = jwtService.generateRefreshToken(customUserDetails);
        tokenRedisService.saveRefreshToken(userSaver.getUsername(), refreshToken);
        String nameAvatar = getAvatar();
        return AuthenticationResponse.builder()
                .userResponse(userResponse)
                .accessToken(jwtToken)
                .avatar(nameAvatar)
                .build();
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
            tokenRedisService.saveRefreshToken(user.getUsername(), refreshToken);

        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .userResponse(userDTO)
                .avatar(avatarService.findByStatus(true).getName())
                .build();
    }

    private String getAvatar(){
        AvatarResponse avatarResponse = avatarService.findByStatus(true);
        if (avatarResponse != null)
            return avatarResponse.getName();
        return null;
    }
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken;
        final String userName;
        if (authHeader == null || !authHeader.startsWith("Bearer"))
            throw new AppException(ErrorCode.TOKEN_INVALID);
        accessToken = authHeader.substring(7);
        userName = jwtService.extractUserName(accessToken);
        if (userName != null) {
            customUserDetails = new CustomUserDetails();
            UserEntity user = modelMapper.map(userService.findByUsername(userName), UserEntity.class);
            customUserDetails.setUser(user);

            String refreshToken = tokenRedisService.getRefreshToken(userName);
            if (refreshToken == null) throw new AppException(ErrorCode.RE_TOKEN_EXPIRED);
            String newAccessToken = jwtService.generateToken(customUserDetails);
            String newRefreshToken = jwtService.generateRefreshToken(customUserDetails);
            tokenRedisService.saveRefreshToken(user.getUsername(), newRefreshToken);
            String nameAvatar = getAvatar();
            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .avatar(nameAvatar)
                    .build();
        }
        return null;
    }
}
