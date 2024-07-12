package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.ImageDataDTO;
import com.myweb.MusicLD.entity.ImageDataEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.StorageRepository;
import com.myweb.MusicLD.service.StorageService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageImpl implements StorageService {
    private final StorageRepository storageRepository;
    private final ModelMapper mapper;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            ImageDataEntity imageData = storageRepository.save(ImageDataEntity.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                            .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(),UserEntity.class))
                    .build());
            return "file upload successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] downloadImage(String fileName) {
        Optional<ImageDataEntity> dbImageData =  storageRepository.findByName(fileName);
        return ImageUtils.decompressImage(dbImageData.get().getImageData());
    }
}
