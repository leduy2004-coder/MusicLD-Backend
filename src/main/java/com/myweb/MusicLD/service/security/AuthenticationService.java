package com.myweb.MusicLD.service.security;

import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.AuthenticationRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.UserRepository;
import com.myweb.MusicLD.service.TokenRedisService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private CustomUserDetails customUserDetails = new CustomUserDetails();

    private final ModelMapper modelMapper;
    private final TokenRedisService tokenRedisService;


    public AuthenticationResponse register(UserRequest request) {
        UserEntity userSaver = userService.insert(request);
        customUserDetails.setUser(userSaver);
        var jwtToken = jwtService.generateToken(customUserDetails);
        var refreshToken = jwtService.generateRefreshToken(customUserDetails);
        tokenRedisService.saveRefreshToken(userSaver.getUsername(), refreshToken);

        return AuthenticationResponse.builder()
                .userResponse(modelMapper.map(userSaver, UserResponse.class))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var jwtToken = "";
        var refreshToken = "";
        UserResponse userResponse;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            customUserDetails = new CustomUserDetails();
            userResponse = userService.findByUsername(request.getUsername());
            UserEntity user = userRepository.findByUsername(request.getUsername()).orElse(null);

            customUserDetails.setUser(user);
            jwtToken = jwtService.generateToken(customUserDetails);
            refreshToken = jwtService.generateRefreshToken(customUserDetails);
            assert user != null;
            tokenRedisService.saveRefreshToken(user.getUsername(), refreshToken);

        } catch (Exception e) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userResponse(userResponse)
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        final String accessToken;
        final String userName;
        if (authHeader != null) {
            authHeader = authHeader.replaceAll("^\"|\"$", "");
            if (authHeader.startsWith("Bearer ")) {
                accessToken = authHeader.substring(7);
            } else
                throw new AppException(ErrorCode.TOKEN_INVALID);
        } else
            throw new AppException(ErrorCode.TOKEN_INVALID);
        userName = jwtService.extractUserName(accessToken);

        if (userName != null) {
            customUserDetails = new CustomUserDetails();
            UserEntity user = userRepository.findByUsername(userName).orElse(null);
            customUserDetails.setUser(user);

            String refreshToken = tokenRedisService.getRefreshToken(userName);
            if (refreshToken == null) throw new AppException(ErrorCode.RE_TOKEN_EXPIRED);
            String newAccessToken = jwtService.generateToken(customUserDetails);
            String newRefreshToken = jwtService.generateRefreshToken(customUserDetails);
            assert user != null;
            tokenRedisService.saveRefreshToken(user.getUsername(), newRefreshToken);

            return AuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
        return null;
    }
}
