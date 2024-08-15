package com.myweb.MusicLD.service.security;

import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.*;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.AvatarRepository;
import com.myweb.MusicLD.repository.UserRepository;
import com.myweb.MusicLD.repository.feignClient.FacebookIdentityClient;
import com.myweb.MusicLD.repository.feignClient.FacebookUserInfoClient;
import com.myweb.MusicLD.repository.feignClient.GoogleIdentityClient;
import com.myweb.MusicLD.repository.feignClient.GoogleUserInfoClient;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.TokenRedisService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.impl.JwtService;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;
    private final TokenRedisService tokenRedisService;
    private final UserRepository userRepository;
    private final GoogleIdentityClient googleIdentityClient;
    private final GoogleUserInfoClient googleUserInfoClient;
    private final FacebookUserInfoClient facebookUserInfoClient;
    private final FacebookIdentityClient facebookIdentityClient;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    protected String CLIENT_ID_FACEBOOK_CLIENT;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    protected String CLIENT_ID_GOOGLE_CLIENT;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    protected String CLIENT_SECRET_FACEBOOK_CLIENT;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    protected String CLIENT_SECRET_GOOGLE_CLIENT;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    protected String REDIRECT_URI_FACEBOOK_CLIENT;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    protected String REDIRECT_URI_GOOGLE_CLIENT;
    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";
    String urlAvatar = null;
    private AvatarEntity avatarEntity;

    public AuthenticationResponse getUserInfo(String provider, String code) {
        Oauth2UserResponse oauth2UserResponse = new Oauth2UserResponse();
        String accessToken = outboundAuthenticate(provider, code);
        if ("google".equals(provider)) {
            Oauth2UserResponse.GoogleUserInfo googleUserInfo = googleUserInfoClient.getUserInfo(accessToken);
            oauth2UserResponse.setGoogleUserInfo(googleUserInfo);
        } else if ("facebook".equals(provider)) {
            Oauth2UserResponse.FacebookUserInfo facebookUserInfo = facebookUserInfoClient.getUserInfo(accessToken, "id,name,picture");
            oauth2UserResponse.setFacebookUserInfo(facebookUserInfo);
        } else {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
        return loadUser(oauth2UserResponse, provider);
    }

    public String outboundAuthenticate(String provider, String code) {
        String accessToken = null;

        if ("google".equals(provider)) {
            Map<String, String> request = new HashMap<>();
            request.put("code", code);
            request.put("client_id", CLIENT_ID_GOOGLE_CLIENT);
            request.put("client_secret", CLIENT_SECRET_GOOGLE_CLIENT);
            request.put("redirect_uri", REDIRECT_URI_GOOGLE_CLIENT);
            request.put("grant_type", GRANT_TYPE);
            ExchangeTokenResponse.ExchangeTokenGoogle response = googleIdentityClient.exchangeToken(request);
            accessToken = response.getAccessToken();
        } else if ("facebook".equals(provider)) {
            ExchangeTokenResponse.ExchangeTokenFaceBook response = facebookIdentityClient.exchangeToken(
                    code,
                    CLIENT_ID_FACEBOOK_CLIENT,
                    CLIENT_SECRET_FACEBOOK_CLIENT,
                    REDIRECT_URI_FACEBOOK_CLIENT,
                    GRANT_TYPE
            );
            accessToken = response.getAccessToken();
        }

        return accessToken;
    }

    public AuthenticationResponse loadUser(Oauth2UserResponse userRequest, String provider) {
        UserEntity userEntity = convertToUserEntity(userRequest, provider);

        UserEntity existingUser = userRepository.findByUsername(userEntity.getUsername()).orElse(null);
        if (existingUser != null) {
            tokenRedisService.clearByUserName(existingUser.getNickName());
            avatarEntity= new AvatarEntity();
        } else {
            existingUser = userService.insert(modelMapper.map(userEntity, UserRequest.class));
            avatarEntity = avatarRepository.save(AvatarEntity.builder()
                    .url(urlAvatar)
                    .userEntity(existingUser)
                    .status(true)
                    .build());
        }
        return loginOauth2(existingUser, avatarEntity);
    }

    public UserEntity convertToUserEntity(Oauth2UserResponse oauth2User, String clientName) {
        String provider = clientName.equalsIgnoreCase("google") ? AuthenticationType.GOOGLE.name() : AuthenticationType.FACEBOOK.name();
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setCode("USER");
        roles.add(role);

        Object userInfo = null;

        if ("GOOGLE".equalsIgnoreCase(provider)) {
            userInfo = oauth2User.getGoogleUserInfo();
        } else {
            userInfo = oauth2User.getFacebookUserInfo();
        }

        String id = null;
        String username = null;

        if (userInfo instanceof Oauth2UserResponse.GoogleUserInfo googleUserInfo) {
            id = googleUserInfo.getId();
            username = googleUserInfo.getName();
            urlAvatar = googleUserInfo.getPicture();
        } else {
            Oauth2UserResponse.FacebookUserInfo facebookUserInfo = (Oauth2UserResponse.FacebookUserInfo) userInfo;
            id = facebookUserInfo.getId();
            username = facebookUserInfo.getName();
            urlAvatar = facebookUserInfo.getPicture().getData().getUrl();
        }

        return UserEntity.builder()
                .authType(AuthenticationType.valueOf(provider))
                .nickName(username)
                .status(true)
                .username(id)
                .roles(roles)
                .build();
    }

    public AuthenticationResponse loginOauth2(UserEntity user, AvatarEntity avatarEntity) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);
        String accessToken = jwtService.generateToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        assert user != null;
        tokenRedisService.saveRefreshToken(user.getUsername(), String.valueOf(refreshToken));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        if (avatarEntity.getUrl() != null){
            userResponse.setAvatar(AvatarResponse.builder()
                    .publicId(avatarEntity.getPublicId())
                    .url(avatarEntity.getUrl())
                    .build());
        }else {
            userResponse.setAvatar(avatarService.findByStatus(user.getId(),true));
        }
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .userResponse(userResponse)
                .build();
    }

}