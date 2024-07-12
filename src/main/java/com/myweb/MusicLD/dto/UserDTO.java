package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.entity.ImageDataEntity;
import com.myweb.MusicLD.entity.PaymentEntity;
import com.myweb.MusicLD.utility.AuthenticationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private List<PaymentEntity> payments = new ArrayList<>();
    private List<ImageDataEntity> files = new ArrayList<>();

}
