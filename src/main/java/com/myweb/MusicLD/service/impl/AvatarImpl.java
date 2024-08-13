package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.AvatarRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AvatarImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;


    @Override
    @Transactional
    public AvatarResponse uploadImage(MultipartFile file) {
        updatedAvatars();
        ImageUtils.assertAllowed(file, ImageUtils.IMAGE_PATTERN);
        String fileName = ImageUtils.getFileName(file.getOriginalFilename());
        CloudinaryResponse response = cloudinaryService.uploadFile(file, fileName);
        avatarRepository.save(AvatarEntity.builder()
                .name(fileName)
                .url(response.getUrl())
                .publicId(response.getPublicId())
                .type(file.getContentType())
                .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                .status(true)
                .build());
        return AvatarResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .build();
    }

    @Override
    public AvatarResponse findByStatus(BigInteger id, Boolean status) {
        List<AvatarEntity> avatarEntity = avatarRepository.findByStatusAndUser(id, status);
        if (avatarEntity.isEmpty()) return null;
        return mapper.map(avatarEntity.getLast(), AvatarResponse.class);
    }

    @Override
    public Boolean deleteImage(String publicId) {
        updatedAvatars();
        cloudinaryService.deleteFile(publicId);
        return true;
    }


    private void updatedAvatars() {
        BigInteger userId = Objects.requireNonNull(GetInfo.getLoggedInUserInfo()).getId();
        List<AvatarEntity> activeAvatars = avatarRepository.findByStatusAndUser(userId, true);
        if (!activeAvatars.isEmpty()) {
            Stream<AvatarEntity> avatarEntityStream = activeAvatars.stream()
                    .map(avatarEntity -> {
                        avatarEntity.setStatus(false);
                        return avatarRepository.save(avatarEntity);
                    });
        }
    }

}
