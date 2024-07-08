package com.myweb.MusicLD.controller.Auth;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {

    private String username;
    private String password;
}