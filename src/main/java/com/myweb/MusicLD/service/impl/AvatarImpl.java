package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.jpa.AvatarRepository;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.ImageUtils;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvatarImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;


    @Override
    @Transactional
    public AvatarResponse uploadImage(MultipartFile file, AvatarType type, MusicEntity musicEntity) {
        updatedAvatars(type, musicEntity.getId());
        ImageUtils.assertAllowed(file, ImageUtils.IMAGE_PATTERN);
        String fileName = ImageUtils.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
        if (type.equals(AvatarType.USER)) {
            avatarRepository.save(AvatarEntity.builder()
                    .name(fileName)
                    .url(response.getUrl())
                    .publicId(response.getPublicId())
                    .type(type)
                    .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                    .status(true)
                    .build());
        } else {
            avatarRepository.save(AvatarEntity.builder()
                    .name(fileName)
                    .url(response.getUrl())
                    .publicId(response.getPublicId())
                    .musicEntity(musicEntity)
                    .type(type)
                    .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                    .status(true)
                    .build());
        }
        return AvatarResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @Transactional
    public AvatarResponse uploadImageUser(MultipartFile file, BigInteger id) {
        updatedAvatars(AvatarType.USER, id);
        UserEntity user = userRepository.findById(id)
                .orElse(null);
        ImageUtils.assertAllowed(file, ImageUtils.IMAGE_PATTERN);
        String fileName = ImageUtils.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);

        avatarRepository.save(AvatarEntity.builder()
                .name(fileName)
                .url(response.getUrl())
                .publicId(response.getPublicId())
                .type(AvatarType.USER)
                .userEntity(user)
                .status(true)
                .build());

        return AvatarResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .build();
    }

    @Override
    public AvatarResponse findByStatus(BigInteger id, Boolean status, AvatarType type) {
        List<AvatarEntity> avatarEntity;
        if (type.equals(AvatarType.USER)) {
            avatarEntity = avatarRepository.findByStatusAndUser(id, status, type);
        } else
            avatarEntity = avatarRepository.findByStatusAndMusic(id, status, type);

        if (avatarEntity.isEmpty()) return null;
        return mapper.map(avatarEntity.getLast(), AvatarResponse.class);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public Boolean deleteImage(String publicId, AvatarType type, BigInteger id) {
        updatedAvatars(type, id);
        cloudinaryService.deleteFile(publicId, "image");
        return true;
    }

    @Override
    public void updatedAvatars(AvatarType type, BigInteger id) {
        List<AvatarEntity> activeAvatars;
        if (type.equals(AvatarType.USER)) {
            activeAvatars = avatarRepository.findByStatusAndUser(id, true, type);
        } else {
            activeAvatars = avatarRepository.findByStatusAndMusic(id, true, type);
        }
        if (!activeAvatars.isEmpty()) {
            activeAvatars.forEach(avatarEntity -> {
                avatarEntity.setStatus(false);
                avatarRepository.save(avatarEntity);
            });
        }

    }


}
