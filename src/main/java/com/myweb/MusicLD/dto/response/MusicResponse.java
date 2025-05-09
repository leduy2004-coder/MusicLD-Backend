package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MusicResponse {
    BigInteger id;
    String publicId;
    String url;
    String title;
    String lyrics;
    int duration;
    String nickName;
    BigInteger idUser;
    AccessMusic access;
    AvatarResponse avatarResponse;
    AvatarResponse userAvatarResponse;
    long countLike;
    boolean isLike;
    boolean status;
}
