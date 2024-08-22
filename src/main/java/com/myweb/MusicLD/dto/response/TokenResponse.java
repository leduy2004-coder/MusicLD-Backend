package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.enumUtils.TokenType;
import lombok.*;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    private BigInteger id;
    private String refreshToken;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
}
