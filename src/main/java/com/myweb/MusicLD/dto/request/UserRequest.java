package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {
    private BigInteger id;
    private String username;
    private String password;
    @Builder.Default
    private Boolean status = true;
    private String nickName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;
    private AuthenticationType authType;
    private List<RoleRequest> roles;
}
