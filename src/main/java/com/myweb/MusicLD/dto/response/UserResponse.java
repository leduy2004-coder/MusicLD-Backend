package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserResponse{
    BigInteger id;
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
    String email;
}
