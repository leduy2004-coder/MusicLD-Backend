package com.myweb.MusicLD.config.Oauth2;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.request.TokenRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.TokenResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.TokenEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.service.TokenService;
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
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = oauth2User.getAccessToken();
        try {
            UserResponse existingUser = userService.findByUsername(oauth2User.getUser().getUsername());
            if (existingUser!=null) {
                revokeAllUserTokens(modelMapper.map(existingUser,UserEntity.class));
            } else {
                UserResponse userDTO = userService.insert(modelMapper.map(oauth2User.getUser(), UserRequest.class));
                oauth2User = new CustomOAuth2User(modelMapper.map(userDTO, UserEntity.class), oauth2User.getOauth2User(), oauth2User.getOauth2ClientName(), oauth2User.getAuthorities(), accessToken);
            }
            updateSecurityContext(oauth2User, clientRegistrationId);
            saveUserToken(oauth2User.getUser(),accessToken);
            this.setAlwaysUseDefaultTargetUrl(true);
//        this.setDefaultTargetUrl("/default-url"); // Đặt URL mặc định của bạn ở đây
            super.onAuthenticationSuccess(request, response, authentication);
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error handling user authentication", ex);
        }
    }

    private void updateSecurityContext(CustomOAuth2User oauth2User, String clientRegistrationId) {
        Authentication securityAuth = new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), clientRegistrationId);
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }

    public void saveUserToken(UserEntity user, String jwtToken) {
        var token = TokenEntity.builder()
                .userEntity(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.save(modelMapper.map(token, TokenRequest.class));
    }
    private void revokeAllUserTokens(UserEntity user) {
        List<TokenResponse> validUserTokens = tokenService.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            tokenService.save(modelMapper.map(token,TokenRequest.class));
        });

    }
}
