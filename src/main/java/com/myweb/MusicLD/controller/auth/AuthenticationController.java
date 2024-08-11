package com.myweb.MusicLD.controller.auth;

import com.myweb.MusicLD.dto.request.AuthenticationRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.service.security.OAuth2UserService;
import com.myweb.MusicLD.service.security.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final OAuth2UserService oAuth2UserService;


    @PostMapping("/authenticate")
    public ApiResponse<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(service.authenticate(request)).build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ApiResponse.<AuthenticationResponse>builder().result(service.refreshToken(request, response)).build();
    }

    @PostMapping("/oauth2")
    ApiResponse<AuthenticationResponse> outboundAuthenticate(
            @RequestParam("code") String code,
            @RequestParam("provider") String provider
    ){
        var result = oAuth2UserService.getUserInfo(provider,code);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }


}