package com.myweb.MusicLD.config.Oauth2;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.TokenResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.service.TokenRedisService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.TokenType;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final TokenRedisService tokenRedisService;
    private final ModelMapper modelMapper;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = oauth2User.getAccessToken();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService
                .loadAuthorizedClient(clientRegistrationId, oAuth2AuthenticationToken.getName());
        String jwtAccessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        String jwtRefrechToken = Objects.requireNonNull(oAuth2AuthorizedClient.getRefreshToken()).getTokenValue();
        try {
            UserResponse existingUser = userService.findByUsername(oauth2User.getUser().getUsername());
            if (existingUser!=null) {
                tokenRedisService.clearByUserName(existingUser.getNickName());
            } else {
                existingUser = userService.insert(modelMapper.map(oauth2User.getUser(), UserRequest.class));
            }
            oauth2User = new CustomOAuth2User(modelMapper.map(existingUser, UserEntity.class), oauth2User.getOauth2User(), oauth2User.getOauth2ClientName(), oauth2User.getAuthorities(), accessToken);
            updateSecurityContext(oauth2User, clientRegistrationId);
            tokenRedisService.saveRefreshToken(oauth2User.getUser().getUsername(), String.valueOf(jwtRefrechToken));
            response.sendRedirect("http://localhost:3000/oauth2/callback?id="+oauth2User.getUser().getId());
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error handling user authentication", ex);
        }
    }


    private void updateSecurityContext(CustomOAuth2User oauth2User, String clientRegistrationId) {
        Authentication securityAuth = new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), clientRegistrationId);
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }

}
