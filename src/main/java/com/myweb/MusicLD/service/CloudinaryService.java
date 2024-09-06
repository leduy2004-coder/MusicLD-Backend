package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    CloudinaryResponse uploadFile(final MultipartFile file, final String fileName);
    void deleteFile(String publicId,String resourceType);
}
