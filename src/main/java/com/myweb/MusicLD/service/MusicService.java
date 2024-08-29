package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;


public interface MusicService {
     MusicResponse uploadMusic(MusicRequest musicRequest);

     List<MusicResponse> findByStatus(BigInteger id, Boolean status);
     Boolean deleteMusic(String pIdAvatar,String pIdMusic );
}
