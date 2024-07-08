package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO extends BaseDTO{
    private String username;
    private String password;
    private Boolean status;
    private String fullName;
    private List<RoleDTO> roles;
    private List<TokenDTO> tokens;
    private AuthenticationType authType;
}
