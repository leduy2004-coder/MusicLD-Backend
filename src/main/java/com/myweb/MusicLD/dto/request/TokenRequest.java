package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.utility.TokenType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenRequest {
    private Long id;
    private String token;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    private UserResponse userEntity;
}
