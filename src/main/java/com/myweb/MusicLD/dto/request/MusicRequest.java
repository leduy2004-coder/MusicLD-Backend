package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    String title;
    String lyrics;
    MultipartFile fileAvatar;
    MultipartFile fileMusic;
    String publicIdAvatar;

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
