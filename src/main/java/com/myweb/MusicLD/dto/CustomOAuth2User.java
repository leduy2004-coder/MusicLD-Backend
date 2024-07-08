package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class CustomOAuth2User implements OAuth2User {

    private UserEntity user;
    private String oauth2ClientName;
    private OAuth2User oauth2User;
    private String accessToken;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2User(UserEntity user,OAuth2User oauth2User, String oauth2ClientName, Collection<? extends GrantedAuthority> authorities, String accessToken) {
        this.user = user;
        this.oauth2User = oauth2User;
        this.oauth2ClientName = oauth2ClientName;
        this.authorities = authorities;
        this.accessToken = accessToken;
    }
    public CustomOAuth2User(OAuth2User oauth2User, String oauth2ClientName, Collection<? extends GrantedAuthority> authorities, String accessToken) {
        this.oauth2User = oauth2User;
        this.oauth2ClientName = oauth2ClientName;
        this.authorities = authorities;
        this.accessToken = accessToken;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getFullName() {
        return oauth2User.getAttribute("name");
    }
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

}
