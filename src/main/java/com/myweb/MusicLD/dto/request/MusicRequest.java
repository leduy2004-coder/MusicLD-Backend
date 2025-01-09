package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MusicRequest {
    BigInteger id;
    BigInteger userId;
    String title;
    String lyrics;
    MultipartFile fileAvatar;
    MultipartFile fileMusic;
    String publicIdAvatar;
    String accessMusic;
    Boolean status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class MusicRequestDelete {
        BigInteger id;
        String publicIdMusic;
        String publicIdAvatar;
    }
}
