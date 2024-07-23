package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.TokenType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    private Long id;
    private String token;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    private UserRequest userEntity;
}
