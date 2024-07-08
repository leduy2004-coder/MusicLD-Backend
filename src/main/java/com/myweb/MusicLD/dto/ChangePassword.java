package com.myweb.MusicLD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
