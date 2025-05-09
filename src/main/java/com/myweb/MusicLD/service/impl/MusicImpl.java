package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.StatisticResponse;
import com.myweb.MusicLD.entity.AvatarEntity;
import com.myweb.MusicLD.entity.MusicEntity;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.repository.jpa.MusicRepository;
import com.myweb.MusicLD.repository.jpa.UserRepository;
import com.myweb.MusicLD.service.AvatarService;
import com.myweb.MusicLD.service.CloudinaryService;
import com.myweb.MusicLD.service.HeartService;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.TupleMapper;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import jakarta.persistence.Tuple;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicImpl implements MusicService {
    AvatarService avatarService;
    UserRepository userRepository;
    MusicRepository musicRepository;
    ModelMapper mapper;
    CloudinaryService cloudinaryService;
    HeartService heartService;

    @Override
    @Transactional
    public MusicResponse uploadMusic(MusicRequest musicRequest) throws IOException, UnsupportedAudioFileException {
        UserEntity user;
        if (musicRequest.getUserId() != null) {
            user = userRepository.findById(musicRequest.getUserId()).orElse(null);
        } else {
            user = userRepository.findByUsername(GetInfo.getLoggedInUserName()).orElse(null);
        }
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
                .userEntity(user)
                .status(true)
                .access(accessMusic)
                .build());
        AvatarResponse avatarResponse = avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music);
        return MusicResponse.builder()
                .publicId(response.getPublicId())
                .url(response.getUrl())
                .idUser(music.getUserEntity().getId())
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
        musicResponse.setAvatarResponse(avatarService.findByStatus(musicId, true, AvatarType.MUSIC));
        musicResponse.setUserAvatarResponse(avatarService.findByStatus(music.getUserEntity().getId(), true, AvatarType.USER));
        musicResponse.setNickName(music.getUserEntity().getNickName());
        musicResponse.setIdUser(music.getUserEntity().getId());
        musicResponse.setLike(heartService.checkLike(Objects.requireNonNull(
                userRepository.findByUsername(GetInfo.getLoggedInUserName()).orElse(null)).getId(), musicResponse.getId()));
        musicResponse.setCountLike(heartService.countLike(musicId));
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
        return mapMusicEntitiesToResponses(musics);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Boolean deleteMusic(BigInteger id) {
        MusicEntity music = musicRepository.findById(id).orElse(null);
        assert music != null;
        cloudinaryService.deleteFile(music.getPublicId(), "video");
        for (AvatarEntity avatarEntity : music.getAvatars()) {
            avatarService.deleteImage(avatarEntity.getPublicId(), avatarEntity.getId());
        }
        musicRepository.deleteById(music.getId());
        return true;
    }

    @Override
    public Boolean updateStatusMusic(BigInteger id, Boolean status) {
        avatarService.updatedAvatars(AvatarType.MUSIC, id);
        musicRepository.updateStatus(id, status);
        return true;
    }

    @Override
    public MusicResponse updateMusic(MusicRequest musicRequest) {
        AccessMusic accessMusic = parseAccessMusic(musicRequest.getAccessMusic());
        MusicEntity music = musicRepository.findById(musicRequest.getId()).orElse(null);
        assert music != null;
        if (musicRequest.getStatus() != null) {
            music.setStatus(musicRequest.getStatus());
        }
        music.setLyrics(musicRequest.getLyrics());
        music.setTitle(musicRequest.getTitle());
        music.setAccess(accessMusic);
        musicRepository.save(music);
        MusicResponse musicResponse = mapper.map(music, MusicResponse.class);
        if (musicRequest.getFileAvatar() != null) {
            avatarService.updatedAvatars(AvatarType.MUSIC, musicRequest.getId());
            musicResponse.setAvatarResponse(avatarService.uploadImage(musicRequest.getFileAvatar(), AvatarType.MUSIC, music));
        }
        return musicResponse;
    }

    @Override
    public long countMusic(BigInteger id) {
        return musicRepository.countMusic(true, id);
    }

    @Override
    public List<MusicResponse> getTopMusics() {
        Pageable pageable = PageRequest.of(0, 20);
        List<MusicEntity> listMusics = musicRepository.getTopMusicsByHeart(pageable);
        return mapMusicEntitiesToResponses(listMusics);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<MusicResponse> findAll() {
        List<MusicEntity> musics = musicRepository.findAll();
        return mapMusicEntitiesToResponses(musics);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<StatisticResponse> getCountMusicByYear(int year) {
        List<Tuple> list = musicRepository.getCountMusicsByYear(year);
        return TupleMapper.mapListToDto(list, StatisticResponse.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public StatisticResponse getStatisticByYear(int year) {
        Tuple tuple = musicRepository.getStatisticByYear(year);
        return TupleMapper.mapToDto(tuple, StatisticResponse.class);
    }

    public List<MusicResponse> mapMusicEntitiesToResponses(List<MusicEntity> musics) {

        String loggedInUsername = GetInfo.getLoggedInUserName();
        Optional<UserEntity> loggedInUser = userRepository.findByUsername(loggedInUsername);

        // Tập hợp các ID nhạc
        List<BigInteger> musicIds = musics.stream()
                .map(MusicEntity::getId)
                .collect(Collectors.toList());

        // Lấy số lượng like và trạng thái like cho từng bài hát
        Map<BigInteger, Long> likesCountMap = heartService.countLikesForMusicIds(musicIds); // Giả sử có phương thức này
        Map<BigInteger, Boolean> userLikesMap = loggedInUser.map(user ->
                heartService.checkLikesForUser(user.getId(), musicIds) // Giả sử có phương thức này
        ).orElse(new HashMap<>());

        return musics.stream()
                .map(musicEntity -> {
                    MusicResponse musicResponse = mapper.map(musicEntity, MusicResponse.class);

                    musicResponse.setIdUser(musicEntity.getUserEntity().getId());
                    musicResponse.setUserAvatarResponse(
                            avatarService.findByStatus(musicResponse.getIdUser(), true, AvatarType.USER)
                    );
                    musicResponse.setNickName(Objects.requireNonNull(userRepository.findById(musicResponse.getIdUser()).orElse(null)).getNickName());
                    musicResponse.setCountLike(likesCountMap.getOrDefault(musicResponse.getId(), 0L));
                    musicResponse.setLike(userLikesMap.getOrDefault(musicResponse.getId(), false));
                    musicResponse.setAvatarResponse(
                            avatarService.findByStatus(musicResponse.getId(), true, AvatarType.MUSIC)
                    );

                    return musicResponse;
                }).collect(Collectors.toList());
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
