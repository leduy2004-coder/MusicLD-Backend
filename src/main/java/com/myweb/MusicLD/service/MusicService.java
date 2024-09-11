package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.UserResponse;

import java.math.BigInteger;
import java.util.List;


public interface MusicService {
     MusicResponse uploadMusic(MusicRequest musicRequest);

     List<MusicResponse> findByStatus(BigInteger id, Boolean status);
     Boolean deleteMusic(String pIdAvatar,String pIdMusic,BigInteger id );
     MusicResponse updateById(MusicRequest musicRequest);

}
