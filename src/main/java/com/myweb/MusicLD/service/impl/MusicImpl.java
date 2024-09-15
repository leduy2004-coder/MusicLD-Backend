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
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.tritonus.share.sampled.file.TAudioFileFormat;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
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
    public MusicResponse uploadMusic(MusicRequest musicRequest) throws IOException, UnsupportedAudioFileException {
        AccessMusic accessMusic = parseAccessMusic(musicRequest.getAccessMusic());
        CloudinaryResponse response = cloudinaryService.uploadFile(musicRequest.getFileMusic(), UUID.randomUUID().toString());
        File file = convertMultipartFileToFile(musicRequest.getFileMusic());
        int duration = getDurationWithMp3Spi(file);
        MusicEntity music = musicRepository.save(MusicEntity.builder()
                .title(musicRequest.getTitle())
                .url(response.getUrl())
                .lyrics(musicRequest.getLyrics())
                .publicId(response.getPublicId())
                .duration(duration)
                .userEntity(mapper.map(GetInfo.getLoggedInUserInfo(), UserEntity.class))
                .status(true)
                .access(accessMusic)
                .build());
        AvatarResponse avatarResponse = avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music);
        return MusicResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .avatarResponse(avatarResponse)
                .build();
    }

    @Override
    public MusicResponse findMusicById(BigInteger musicId) {
        MusicEntity music = musicRepository.findById(musicId)
                .orElse(null);
        if (music == null) {
            return null;
        }
        MusicResponse musicResponse = mapper.map(music, MusicResponse.class);
        musicResponse.setAvatarResponse(avatarService.findByStatus(musicId, true,AvatarType.MUSIC));
        musicResponse.setNickName(music.getUserEntity().getNickName());
        return musicResponse;
    }

    @Override
    public List<MusicResponse> findByStatus(BigInteger id, Boolean status, String accessMusic) {
        List<MusicEntity> musics;
        AccessMusic access = parseAccessMusic(accessMusic);
        if (accessMusic != null)
            musics = musicRepository.findByStatusAndMusicAndAccess(id, status, access);
        else
            musics = musicRepository.findByStatusAndMusic(id, status);
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
        cloudinaryService.deleteFile(pIdMusic, "video");
        avatarService.deleteImage(pIdAvatar, AvatarType.MUSIC, id);
        musicRepository.updateStatus(id, false);
        return true;
    }

    @Override
    public MusicResponse updateById(MusicRequest musicRequest) {
        AccessMusic accessMusic = parseAccessMusic(musicRequest.getAccessMusic());
        MusicEntity music = musicRepository.findById(musicRequest.getId()).orElse(null);
        assert music != null;
        music.setLyrics(musicRequest.getLyrics());
        music.setTitle(musicRequest.getTitle());
        music.setAccess(accessMusic);
        musicRepository.save(music);
        MusicResponse musicResponse = mapper.map(music, MusicResponse.class);
        if (musicRequest.getFileAvatar() != null) {
            avatarService.deleteImage(musicRequest.getPublicIdAvatar(), AvatarType.MUSIC, musicRequest.getId());
            musicResponse.setAvatarResponse(avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music));
        }
        return musicResponse;
    }

    private AccessMusic parseAccessMusic(String accessMusic) {
        return AccessMusic.PUBLIC.name().equalsIgnoreCase(accessMusic) ? AccessMusic.PUBLIC : AccessMusic.PRIVATE;
    }

    private int getDurationWithMp3Spi(File file) throws UnsupportedAudioFileException, IOException {
        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            return (int) (microseconds / 1000000);
        } else {
            throw new UnsupportedAudioFileException();
        }
    }
    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Tạo một tệp tạm thời với tên tương tự tên gốc của multipartFile
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);
        return file;
    }

}
