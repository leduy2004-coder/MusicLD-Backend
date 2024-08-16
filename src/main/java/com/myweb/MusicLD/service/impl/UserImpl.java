package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.RoleService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final AvatarService avatarService;

    @Override
    public UserEntity insert(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent())
            throw new AppException(ErrorCode.USER_EXISTED);

        UserEntity userEntity = modelMapper.map(userRequest, UserEntity.class);

        if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {
            List<RoleEntity> roles = userRequest.getRoles().stream()
                    .map(role ->
                            modelMapper.map(roleService.findByCode(role.getCode()), RoleEntity.class))
                    .collect(Collectors.toList());
            userEntity.setRoles(roles);
        }
        if (userRequest.getAuthType().name().equalsIgnoreCase("LOCAL"))
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserResponse findById(BigInteger id) {
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null;
        }
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setAvatar(avatarService.findByStatus(id, true));
        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String userName) {
        UserEntity user = userRepository.findByUsername(userName)
                .orElse(null);
        if (user == null) {
            return null;
        }
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setAvatar(avatarService.findByStatus(user.getId(), true));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    @Transactional
    public void changePassword(ChangePassword request, Principal connectedUser) {
        CustomUserDetails customUserDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), customUserDetails.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }
        customUserDetails.getUser().setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(customUserDetails.getUser());
    }

    @Override
    public List<UserResponse> findAll() {
        List<UserEntity> list = userRepository.findAll();
        return list.stream().map(UserEntity -> modelMapper.map(UserEntity, UserResponse.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username, authType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String searchString) {
        return userRepository.searchUsers(searchString).stream()
                .map(userEntity -> {
                            UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
                            userResponse.setAvatar(avatarService.findByStatus(userEntity.getId(), true));
                            return userResponse;
                        }
                )
                .toList();
    }

}