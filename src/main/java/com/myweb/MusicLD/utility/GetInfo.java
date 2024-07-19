package com.myweb.MusicLD.utility;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GetInfo {
    private static ModelMapper getMapper() {
        return new ModelMapper();
    }

    public static UserResponse getLoggedInUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        ModelMapper mapper = getMapper();
        if (authentication.getPrincipal() instanceof CustomUserDetails userPrincipal) {
            return mapper.map(userPrincipal.getUser(), UserResponse.class);
        } else if (authentication.getPrincipal() instanceof OAuth2User) {
            CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
            return mapper.map(oauth2User.getUser(), UserResponse.class);
        }
        return null;
    }
}
