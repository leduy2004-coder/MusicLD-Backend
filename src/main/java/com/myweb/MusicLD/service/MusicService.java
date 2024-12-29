package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.UserResponse;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


public interface MusicService {
     MusicResponse uploadMusic(MusicRequest musicRequest) throws IOException, UnsupportedAudioFileException;
     MusicResponse findMusicById(BigInteger musicId);
     List<MusicResponse> findByStatus(BigInteger id, Boolean status, String accessMusic);
     Boolean deleteMusic(String pIdAvatar,String pIdMusic,BigInteger id );
     MusicResponse updateById(MusicRequest musicRequest);
     long countMusic(BigInteger id);
     List<MusicResponse> getTopMusics();

}
