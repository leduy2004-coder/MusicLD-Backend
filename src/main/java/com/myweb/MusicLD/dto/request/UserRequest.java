package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private Long id;
    private String username;
    private String password;
    private Boolean status = true;
    private String nickName;
    private Date dateOfBirth;
    private AuthenticationType authType;
    private List<RoleRequest> roles;
}
