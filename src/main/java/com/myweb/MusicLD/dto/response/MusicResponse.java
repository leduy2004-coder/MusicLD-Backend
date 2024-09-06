package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private AvatarResponse avatarResponse;
}
