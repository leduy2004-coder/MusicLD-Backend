package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse{
    private Long id;
    private String username;
    private String password;
    private Boolean status;
    private String nickName;
    private List<RoleResponse> roles;
    private AuthenticationType authType;

}
