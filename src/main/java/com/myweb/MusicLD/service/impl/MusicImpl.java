package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.MusicRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
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

        CloudinaryResponse response = cloudinaryService.uploadFile(musicRequest.getFileMusic(), UUID.randomUUID().toString());
        MusicEntity music = musicRepository.save(MusicEntity.builder()
                .title(musicRequest.getTitle())
                .url(response.getUrl())
                .lyrics(musicRequest.getLyrics())
                .publicId(response.getPublicId())
                .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                .status(true)
                .build());
        AvatarResponse avatarResponse = avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music);
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
                .map(MusicEntity -> {
                    MusicResponse musicResponse = mapper.map(MusicEntity, MusicResponse.class);
                    musicResponse.setAvatarResponse(avatarService.findByStatus(musicResponse.getId(), true, AvatarType.MUSIC));
                    return musicResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteMusic(String pIdMusic, String pIdAvatar, BigInteger id) {
        cloudinaryService.deleteFile(pIdMusic,"video");
        avatarService.deleteImage(pIdAvatar, AvatarType.MUSIC, id);
        musicRepository.updateStatus(id, false);
        return true;
    }

    @Override
    public MusicResponse updateById(MusicRequest musicRequest) {
        MusicEntity music = musicRepository.findById(musicRequest.getId()).orElse(null);
        assert music != null;
        music.setLyrics(musicRequest.getLyrics());
        music.setTitle(musicRequest.getTitle());
        MusicResponse musicResponse = mapper.map(music, MusicResponse.class);
        if(musicRequest.getFileAvatar() !=null){
            avatarService.deleteImage(musicRequest.getPublicIdAvatar(),AvatarType.MUSIC,musicRequest.getId());
            musicResponse.setAvatarResponse(avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music));
        }
        return musicResponse;
    }

}
