package com.myweb.MusicLD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDTO extends BaseDTO{
    private String code;
    private String name;
    private List<UserDTO> users;
}
