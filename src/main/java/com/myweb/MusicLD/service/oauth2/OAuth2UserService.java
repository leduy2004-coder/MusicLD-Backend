package com.myweb.MusicLD.service.oauth2;

import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.UserRepository;
import com.myweb.MusicLD.service.TokenRedisService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.impl.JwtService;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenRedisService tokenRedisService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //login by facebook or google
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User user = super.loadUser(userRequest);
        UserEntity userEntity = convertToUserEntity(user, clientName);

        UserResponse existingUser = userService.findByUsername(userEntity.getUsername());
        if (existingUser!=null) {
            tokenRedisService.clearByUserName(existingUser.getNickName());
        } else {
            userService.insert(modelMapper.map(userEntity, UserRequest.class));
        }


        return user;
    }

    public UserEntity convertToUserEntity(OAuth2User oauth2User, String clientName) {
        String provider = clientName.equals("Google") ? AuthenticationType.GOOGLE.name() : AuthenticationType.FACEBOOK.name();
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setCode("USER");
        roles.add(role);
        String id = null;
        if ("GOOGLE".equalsIgnoreCase(provider)) {
            id = (String) oauth2User.getAttribute("sub");
        } else {
            id = (String) oauth2User.getAttribute("id");
        }
        return UserEntity.builder()
                .authType(AuthenticationType.valueOf(provider))
                .nickName(oauth2User.getAttribute("name"))
                .status(true)
                .username(id)
                .roles(roles)
                .build();
    }

    public AuthenticationResponse loginOauth2(UserRequest userRequest) {
        UserEntity user = userRepository.findByUsername(String.valueOf(userRequest.getId())).orElse(null);
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);
        String accessToken = jwtService.generateToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        assert user != null;
        tokenRedisService.saveRefreshToken(user.getUsername(), String.valueOf(refreshToken));
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .userResponse(userResponse)
                .build();
    }
}