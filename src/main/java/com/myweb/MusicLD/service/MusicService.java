package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.MusicRequest;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.StatisticResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.MusicEntity;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


public interface MusicService {
     MusicResponse uploadMusic(MusicRequest musicRequest) throws IOException, UnsupportedAudioFileException;
     MusicResponse findMusicById(BigInteger musicId);
     List<MusicResponse> findByStatus(BigInteger id, Boolean status, String accessMusic);
     Boolean deleteMusic(MusicEntity musics);
     Boolean updateStatusMusic(BigInteger id);
     MusicResponse updateById(MusicRequest musicRequest);
     long countMusic(BigInteger id);
     List<MusicResponse> getTopMusics();
     List<StatisticResponse> getCountMusicByYear(int year);
     StatisticResponse getStatisticByYear(int year);
}
