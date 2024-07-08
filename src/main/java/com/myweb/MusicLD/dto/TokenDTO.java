package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.utility.TokenType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO extends BaseDTO {
    private String token;
    private TokenType tokenType;
    private boolean revoked;
    private boolean expired;
    private UserDTO userEntity;
}
