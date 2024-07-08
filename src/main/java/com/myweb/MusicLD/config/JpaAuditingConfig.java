package com.myweb.MusicLD.config;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.UserDTO;
import com.myweb.MusicLD.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }

    public static class AuditorAwareImpl implements AuditorAware<Long> {

        @Override
        public Optional<Long> getCurrentAuditor() {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated() ||
                        authentication instanceof AnonymousAuthenticationToken) {
                    return Optional.empty();
                }
                if (authentication.getPrincipal() instanceof CustomUserDetails) {
                    CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
                    return Optional.ofNullable(userPrincipal.getUser().getId());
                } else if (authentication.getPrincipal() instanceof OAuth2User) {
                    CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
                    return Optional.ofNullable(oauth2User.getUser().getId());
                }
                return Optional.empty();
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }
}
