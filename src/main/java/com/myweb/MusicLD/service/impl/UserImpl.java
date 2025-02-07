package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.UserInputDTO;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.dto.response.StatisticResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.*;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.TupleMapper;
import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private final MusicService musicService;
    private final PasswordEncoder passwordEncoder;
    private final AvatarService avatarService;
    private final FollowerService followerService;

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
        userEntity.setStatus(true);
        return userRepository.save(userEntity);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Boolean delete(BigInteger id) {
        try {
            Optional<UserEntity> entity = userRepository.findById(id);
            if (entity.isPresent()) {
                for (AvatarEntity avatarEntity : entity.get().getAvatars()) {
                    avatarService.deleteImage(avatarEntity.getPublicId(), AvatarType.USER, id);
                }
                for (MusicEntity music : entity.get().getMusics()) {
                    musicService.deleteMusic(music.getId());
                }
                userRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserResponse findById(BigInteger id) {
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null;
        }
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setAvatar(avatarService.findByStatus(id, true, AvatarType.USER));
        userResponse.setStatusFollower(followerService.getStatus(user.getId()));
        return userResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public UserInputDTO findUserForAdminById(BigInteger id) {
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        if (user == null) {
            return null;
        }
        UserInputDTO userResponse = modelMapper.map(user, UserInputDTO.class);

        userResponse.setAvatar(avatarService.findByStatus(id, true, AvatarType.USER));
        userResponse.setStatusFollower(followerService.getStatus(user.getId()));
        return userResponse;
    }

    @Override
    public UserResponse findByUsername(String userName) {
        UserEntity user = userRepository.findByUsername(userName)
                .orElse(null);
        if (user == null) {
            return null;
        }
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        userResponse.setRoles(modelMapper.map(user.getRoles().getFirst(), RoleResponse.class));
        userResponse.setAvatar(avatarService.findByStatus(user.getId(), true, AvatarType.USER));
        return userResponse;
    }

    @Override
    @Transactional
    public Boolean changePassword(ChangePassword request) {
        UserEntity user = userRepository.findByUsername(GetInfo.getLoggedInUserName()).orElse(null);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_WRONG);
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new AppException(ErrorCode.PASSWORD_NOT_SAME);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        try {
            UserEntity userEntity = userRepository.save(user);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> findAll() {
        log.info("In method in admin");
        List<UserEntity> list = userRepository.findAll();
        return mapUserEntitiesToResponses(list);
    }

    @Override
    @Transactional
    public void updateAuthenticationType(String username, String oauth2ClientName) {
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
        userRepository.updateAuthenticationType(username, authType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchUsers(String searchString, String type) {
        if (type.equalsIgnoreCase("less")) {
            Pageable topFive = PageRequest.of(0, 5);
            return mapUserEntitiesToResponses(userRepository.findDistinctByRoles_CodeAndNickNameContainingIgnoreCase("USER", searchString, topFive));
        } else {
            return mapUserEntitiesToResponses(userRepository.searchFullUsers(searchString, "USER"));
        }
    }


    @Override
    public UserResponse updateById(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findById(userRequest.getId()).orElseThrow();
        userEntity.setGender(userRequest.getGender());
        userEntity.setDateOfBirth(userRequest.getDateOfBirth());
        userEntity.setNickName(userRequest.getNickName());
        userEntity.setStatus(userRequest.getStatus());
        if (userRequest.getUsername() != null) {
            userEntity.setUsername(userRequest.getUsername());
        }
        if (userRequest.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        if (userRequest.getStatus() != userEntity.getStatus() && userRequest.getStatus()) {
            userEntity.getMusics().forEach(music -> musicService.updateStatusMusic(music.getId(), userRequest.getStatus()));
        }
        UserEntity user = userRepository.save(userEntity);
        return modelMapper.map(user, UserResponse.class);
    }


    @Override
    public List<UserResponse> getTopUsersByFollower() {
        Pageable pageable = PageRequest.of(0, 6);
        return mapUserEntitiesToResponses(userRepository.getTopUsersByFollowers(pageable, "USER"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<StatisticResponse> getTopUserByMusic(int year) {
        List<Tuple> list = userRepository.getTopUsersByMusics(year);
        List<StatisticResponse> responses = TupleMapper.mapListToDto(list, StatisticResponse.class);
        return responses.stream().peek(statistic -> {
            statistic.setAvatar(avatarService.findByStatus(statistic.getUserId().toBigInteger(), true, AvatarType.USER));
        }).toList();
    }

    @Override
    public List<UserResponse> mapUserEntitiesToResponses(List<UserEntity> userEntities) {
        return userEntities.stream()
                .map(userEntity -> {
                    UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
                    userResponse.setCountFollower(userRepository.getCountFollowers(userEntity.getId()));
                    userResponse.setAvatar(avatarService.findByStatus(userEntity.getId(), true, AvatarType.USER));
                    return userResponse;
                })
                .toList();
    }


}