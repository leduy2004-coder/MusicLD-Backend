package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentResponse {
    private BigInteger id;
    private String content;
    private UserResponse userResponse;
    private BigInteger parentId;
    private LocalDateTime createdDate;
}
