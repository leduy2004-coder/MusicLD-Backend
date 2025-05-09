package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    BigInteger id;
    String content;
    UserResponse userResponse;
    BigInteger parentId;
    LocalDateTime createdDate;
    String titleMusic;
    BigInteger musicId;
}
