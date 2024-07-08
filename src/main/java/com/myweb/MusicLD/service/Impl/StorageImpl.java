package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.ImageDataDTO;
import com.myweb.MusicLD.entity.ImageDataEntity;
import com.myweb.MusicLD.repository.StorageRepository;
import com.myweb.MusicLD.service.StorageService;
import com.myweb.MusicLD.utility.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageImpl implements StorageService {
    private final StorageRepository storageRepository;


    @Override
    public String uploadImage(MultipartFile file) {
        try {
            ImageDataEntity imageData = storageRepository.save(ImageDataEntity.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes())).build());
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
