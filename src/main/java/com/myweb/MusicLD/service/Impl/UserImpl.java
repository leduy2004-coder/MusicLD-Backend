package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.UserRepository;
import com.myweb.MusicLD.service.RoleService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.AuthenticationType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CustomUserDetails customUserDetails;

    @Override
    public UserResponse insert(UserRequest userRequest) {

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
            UserEntity savedUserEntity = userRepository.save(userEntity);
            return modelMapper.map(savedUserEntity, UserResponse.class);
    }

    @Override
    public UserResponse findById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByUsername(String userName) {
        UserEntity user = userRepository.findByUsername(userName)
                .orElse(null);
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public void changePassword(ChangePassword request, Principal connectedUser) {
        customUserDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
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
        return list.stream().map(UserEntity -> modelMapper.map(UserEntity,UserResponse.class)).collect(Collectors.toList());
    }

    @Override
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username, authType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String searchString) {
        return userRepository.searchUsers(searchString).stream()
                .map(userEntity -> modelMapper.map(userEntity, UserResponse.class))
                .toList();
    }

}