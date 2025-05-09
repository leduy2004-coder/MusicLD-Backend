package com.myweb.MusicLD.service.security;

import com.myweb.MusicLD.dto.request.AuthenticationRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.impl.JwtService;
import com.myweb.MusicLD.service.redis.TokenRedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserService userService;

    private final ModelMapper modelMapper;
    private final TokenRedisService tokenRedisService;
    private final UserRepository userRepository;


    public AuthenticationResponse register(UserRequest request) {
        UserEntity userSaver = userService.insert(request);

        var jwtToken = jwtService.generateToken(userSaver);
        var refreshToken = jwtService.generateRefreshToken(userSaver);
        tokenRedisService.saveRefreshToken(userSaver.getUsername(), refreshToken);

        UserResponse userResponse = modelMapper.map(userSaver, UserResponse.class);

        userResponse.setRoles(RoleResponse.builder().code("USER").name("user").id(BigInteger.valueOf(2)).build());
        return AuthenticationResponse.builder()
                .userResponse(userResponse)
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var jwtToken = "";
        var refreshToken = "";
        UserResponse userResponse;
        try {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            UserEntity user = userRepository.findByUsername(request.getUsername()).orElse(null);
            assert user != null;
            boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

            if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

            jwtToken = jwtService.generateToken(user);
            refreshToken = jwtService.generateRefreshToken(user);

            tokenRedisService.saveRefreshToken(user.getUsername(), refreshToken);

            userResponse = modelMapper.map(user, UserResponse.class);
            RoleResponse roleResponse = modelMapper.map(user.getRoles().getFirst(), RoleResponse.class);
            userResponse.setRoles(roleResponse);
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
            UserEntity user = userRepository.findByUsername(userName).orElse(null);

            String refreshToken = tokenRedisService.getRefreshToken(userName);
            if (refreshToken == null) throw new AppException(ErrorCode.RE_TOKEN_EXPIRED);
            String newAccessToken = jwtService.generateToken(user);
            String newRefreshToken = jwtService.generateRefreshToken(user);
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
