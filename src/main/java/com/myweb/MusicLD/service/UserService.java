package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.UserResponse;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserResponse insert(UserRequest userDto);

    UserResponse findById(Long id);
    UserResponse findByUsername(String userName);
    public void changePassword(ChangePassword request, Principal connectedUser);
    public List<UserResponse> findAll();
    public void updateAuthenticationType(String username, String oauth2ClientName);
}
