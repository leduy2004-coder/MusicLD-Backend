package com.myweb.MusicLD.config.security;

import com.myweb.MusicLD.config.JpaAuditingConfig;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    @Value("${spring.application.security.jwt.secret-key}")
    private String secretKey;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("ROLE_");
        authoritiesConverter.setAuthoritiesClaimName("roles");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (ExpiredJwtException e) {
                // Có thể xử lý riêng cho token hết hạn
                throw new AppException(ErrorCode.TOKEN_EXPIRED);
            } catch (JwtException e) {
                // Các lỗi JWT khác
                throw new AppException(ErrorCode.TOKEN_INVALID);
            } catch (Exception e) {
                // Các lỗi không xác định khác
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        };
    }
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(secretKey).decode();
        if (keyBytes.length < 32) { // 32 bytes cho HS256
            throw new IllegalArgumentException("Secret key quá ngắn, cần ít nhất 32 bytes.");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }


    @Bean
    public AuditorAware<String> auditorAware() {
        return new JpaAuditingConfig.AuditorAwareImpl();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
