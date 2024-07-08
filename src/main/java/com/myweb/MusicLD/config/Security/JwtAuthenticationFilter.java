package com.myweb.MusicLD.config.Security;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.repository.TokenRepository;
import com.myweb.MusicLD.service.Impl.CustomOAuth2UserService;
import com.myweb.MusicLD.service.Impl.CustomUserDetailService;
import com.myweb.MusicLD.service.Impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;
    private final TokenRepository tokenRepository;
    private final CustomOAuth2UserService oAuth2UserService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userName;
            final String provider;

            if (authHeader == null || !authHeader.startsWith("Bearer")){
                filterChain.doFilter(request,response);
                return;
            }
            jwt = authHeader.substring(7);

            provider = extractProviderFromToken(jwt);
            if (provider != null) {
                CustomOAuth2User oAuth2User = oAuth2UserService.loadUserByToken(jwt, provider);
                if (oAuth2User != null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            oAuth2User, provider, oAuth2User.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            userName = jwtService.extractUserName(jwt);
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails =  this.userDetailsService.loadUserByUsername(userName);
                var isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if (jwtService.isTokenValid(jwt,userDetails) && isTokenValid){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String extractProviderFromToken(String token) {
        if (token.startsWith("ya29")) {
            return "google";
        } else if (token.startsWith("EAAG")) {
            return "facebook";
        }
        return null;
    }
}
