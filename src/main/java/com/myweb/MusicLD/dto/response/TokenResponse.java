package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.enumUtils.TokenType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenResponse {
    BigInteger id;
    String refreshToken;
    TokenType tokenType;
    boolean revoked;
    boolean expired;
}
