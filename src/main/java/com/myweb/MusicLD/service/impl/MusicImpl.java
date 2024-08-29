package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.AvatarRepository;
import com.myweb.MusicLD.repository.MusicRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.ImageUtils;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MusicImpl implements MusicService {
    private final AvatarService avatarService;
    private final MusicRepository musicRepository;
    private final ModelMapper mapper;
    private final CloudinaryService cloudinaryService;


    @Override
    @Transactional
    public MusicResponse uploadMusic(MusicRequest musicRequest) {

        CloudinaryResponse response = cloudinaryService.uploadFile(musicRequest.getFileMusic(), musicRequest.getTitle());
        musicRepository.save(MusicEntity.builder()
                .title(musicRequest.getTitle())
                .url(response.getUrl())
                .lyrics(musicRequest.getLyrics())
                .publicId(response.getPublicId())
                .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                .status(true)
                .build());
        AvatarResponse avatarResponse = avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC);
        return MusicResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .avatarResponse(avatarResponse)
                .build();
    }

    @Override
    public List<MusicResponse> findByStatus(BigInteger id, Boolean status) {
        List<MusicEntity> musics = musicRepository.findByStatusAndMusic(id, status);
        if (musics.isEmpty()) return null;
        return musics.stream()
                .map(UserEntity -> {
                    MusicResponse musicResponse = mapper.map(UserEntity, MusicResponse.class);
                    musicResponse.setAvatarResponse(avatarService.findByStatus(id,true,AvatarType.MUSIC));
                    return musicResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteMusic(String pIdAvatar, String pIdMusic) {
        cloudinaryService.deleteFile(pIdMusic);
        avatarService.deleteImage(pIdAvatar, AvatarType.MUSIC);
        return true;
    }
}
