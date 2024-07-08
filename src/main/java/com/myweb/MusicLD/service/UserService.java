package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.UserDTO;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserDTO insert(UserDTO userDto);

    UserDTO findById(Long id);
    UserDTO findByUsername(String userName);
    public void changePassword(ChangePassword request, Principal connectedUser);
    public List<UserDTO> findAll();
    public void updateAuthenticationType(String username, String oauth2ClientName);
}
