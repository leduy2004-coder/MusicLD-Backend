package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;


public interface AvatarService {
     AvatarResponse uploadImage(MultipartFile file, AvatarType type, MusicEntity music);
     AvatarResponse uploadImageUser(MultipartFile file, BigInteger id);

     AvatarResponse findByStatus(BigInteger id, Boolean status, AvatarType type);
     void deleteImage(String publicId, BigInteger imageId);
     void updatedAvatars(AvatarType type, BigInteger id);
}
