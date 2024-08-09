package com.myweb.MusicLD.utility;

import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
        }
        return null;
    }
}
