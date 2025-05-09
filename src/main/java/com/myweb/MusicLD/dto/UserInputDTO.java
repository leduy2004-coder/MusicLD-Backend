package com.myweb.MusicLD.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInputDTO {
    BigInteger id;
    String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    Date dateOfBirth;
    String nickName;
    Boolean gender;
    RoleResponse roles;
    AvatarResponse avatar;
    RequestFollowStatus statusFollower;
    List<CommentResponse> musicResponses;
    long countFollower;
    boolean status;
}
