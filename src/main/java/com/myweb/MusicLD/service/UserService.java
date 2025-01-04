package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.StatisticResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

public interface UserService {
    UserEntity insert(UserRequest userDto);

    UserResponse findById(BigInteger id);
    UserResponse findByUsername(String userName);
    void changePassword(ChangePassword request, Principal connectedUser);
    List<UserResponse> findAll();
    void updateAuthenticationType(String username, String oauth2ClientName);
    List<UserResponse> searchUsers(String searchString, String type);
    UserResponse updateById(UserRequest userRequest);
    List<UserResponse> getTopUsersByFollower();
    List<StatisticResponse> getTopUserByMusic(int year);
    List<UserResponse> mapUserEntitiesToResponses(List<UserEntity> userEntities);
}
