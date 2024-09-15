package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MusicResponse {
    private BigInteger id;
    private String publicId;
    private String url;
    private String title;
    private String lyrics;
    private int duration;
    private String nickName;
    private AccessMusic access;
    private AvatarResponse avatarResponse;
}
