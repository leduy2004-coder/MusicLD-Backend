package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import org.springframework.web.multipart.MultipartFile;


public interface AvatarService {
     AvatarResponse uploadImage(MultipartFile file);
     byte[] downloadImage(String fileName);
     AvatarResponse findByStatus(Boolean status);
}
