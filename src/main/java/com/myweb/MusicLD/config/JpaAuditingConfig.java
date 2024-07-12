package com.myweb.MusicLD.config;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.UserDTO;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.GetInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Objects;
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
                }else
                    return Optional.ofNullable(Objects.requireNonNull(GetInfo.getLoggedInUserInfo()).getId());
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }
}
