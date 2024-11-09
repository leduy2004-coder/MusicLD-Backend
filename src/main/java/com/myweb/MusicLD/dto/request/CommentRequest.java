package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.UserResponse;
import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {
    private BigInteger id;
    private String content;
    private BigInteger parentId;
    private BigInteger musicId;
}