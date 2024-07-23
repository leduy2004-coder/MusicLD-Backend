package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ModelMapper modelMapper;
    private final UserService userService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //login by facebook or google
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User user =  super.loadUser(userRequest);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new CustomOAuth2User(convertToUserEntity(user,clientName), user, clientName,authorities,userRequest.getAccessToken().getTokenValue());
    }
    public CustomOAuth2User loadUserByToken(String token, String provider) {
        String userInfoEndpointUri = "";

        if (provider.equals("google")) {
            userInfoEndpointUri = "https://www.googleapis.com/oauth2/v3/userinfo";
        } else if (provider.equals("facebook")) {
            userInfoEndpointUri = "https://graph.facebook.com/me?fields=id,name,email";
        }
        if (!userInfoEndpointUri.isEmpty()) {
            Map<String, Object> userAttributes = restTemplate.getForObject(userInfoEndpointUri + "?access_token=" + token, Map.class);
            if (userAttributes != null) {
                UserEntity user = modelMapper.map(userService.findByUsername((String) userAttributes.get("email")), UserEntity.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER"));

                return new CustomOAuth2User(
                        user,
                        new DefaultOAuth2User(authorities, userAttributes, "name"),
                        provider,
                        authorities,
                        token
                );
            }
        }
        return null;
    }

    public UserEntity convertToUserEntity(OAuth2User oauth2User, String clientName) {
        String provider = clientName.equals("Google") ? AuthenticationType.GOOGLE.name() : AuthenticationType.FACEBOOK.name();
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity role = new RoleEntity();
        role.setCode("USER");
        roles.add(role);
        return UserEntity.builder()
                .authType(AuthenticationType.valueOf(provider))
                .fullName(oauth2User.getAttribute("name"))
                .status(true)
                .username(oauth2User.getAttribute("email"))
                .roles(roles)
                .build();
    }

}