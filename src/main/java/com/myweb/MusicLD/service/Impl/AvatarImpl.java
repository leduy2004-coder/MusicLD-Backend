package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.AvatarRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvatarImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final ModelMapper mapper;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            List<AvatarEntity> activeAvatars = avatarRepository.findByStatus(true);
            if(!activeAvatars.isEmpty()) {
                List<AvatarEntity> updatedAvatars = activeAvatars.stream()
                        .map(avatarEntity -> {
                            avatarEntity.setStatus(false);
                            return avatarRepository.save(avatarEntity);
                        }).toList();
            }
            AvatarEntity imageData = avatarRepository.save(AvatarEntity.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .avatarData(ImageUtils.compressImage(file.getBytes()))
                    .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(),UserEntity.class))
                    .status(true)
                    .build());
            return file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadImage(String fileName) {
        Optional<AvatarEntity> dbImageData =  avatarRepository.findByName(fileName);
        return ImageUtils.decompressImage(dbImageData.get().getAvatarData());
    }

    @Override
    public AvatarResponse findByStatus(Boolean status) {
        return mapper.map(avatarRepository.findByStatus(status).getLast(), AvatarResponse.class);
    }


}
