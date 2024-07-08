package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.UserDTO;
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

import java.security.Principal;
import java.util.ArrayList;
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
    public UserDTO insert(UserDTO userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            List<RoleEntity> roles = new ArrayList<>();
            roles = userDto.getRoles().stream().map(role -> modelMapper.map(roleService.findById(role.getId()), RoleEntity.class)).collect(Collectors.toList());
            userEntity.setRoles(roles);
        }
        if (userDto.getAuthType().equals("LOCAL"))
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return modelMapper.map(savedUserEntity, UserDTO.class);
    }

    @Override
    public UserDTO findById(Long id) {
        return modelMapper.map(userRepository.findById(id),UserDTO.class);
    }

    @Override
    public UserDTO findByUsername(String userName) {
        UserEntity userEntity = userRepository.findByUsername(userName).orElse(null);
        if (userEntity == null) {
            return null;
        }
        return modelMapper.map(userEntity, UserDTO.class);
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
    public List<UserDTO> findAll() {
        List<UserEntity> list = userRepository.findAll();
        return list.stream().map(UserEntity -> modelMapper.map(UserEntity,UserDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username, authType);
    }

}