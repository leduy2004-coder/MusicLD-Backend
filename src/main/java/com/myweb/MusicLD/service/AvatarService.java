package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;


public interface AvatarService {
     AvatarResponse uploadImage( MultipartFile file);

     AvatarResponse findByStatus(BigInteger id,Boolean status);
     Boolean deleteImage(String publicId);
}
