package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.UserResponse;
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
public class CommentRequest {
    BigInteger id;
    String content;
    BigInteger parentId;
    BigInteger musicId;
}