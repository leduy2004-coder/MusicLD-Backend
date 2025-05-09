package com.myweb.MusicLD.controller.admin;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.StatisticResponse;
import com.myweb.MusicLD.service.MusicService;
import com.myweb.MusicLD.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    MusicService musicService;
    UserService userService;

    @GetMapping("/get-count-music-by-year")
    public ApiResponse<List<StatisticResponse>> getCountMusicByYear(@RequestParam(value = "year") int year) {
        return ApiResponse.<List<StatisticResponse>>builder().result(musicService.getCountMusicByYear(year)).build();
    }

    @GetMapping("/get-statistic-by-year")
    public ApiResponse<StatisticResponse> getStatistic(@RequestParam(value = "year") int year) {
        return ApiResponse.<StatisticResponse>builder().result(musicService.getStatisticByYear(year)).build();
    }

    @GetMapping("/get-top-user-by-year")
    public ApiResponse<List<StatisticResponse>> getTopUserByYear(@RequestParam(value = "year") int year) {
        return ApiResponse.<List<StatisticResponse>>builder().result(userService.getTopUserByMusic(year)).build();
    }
}
