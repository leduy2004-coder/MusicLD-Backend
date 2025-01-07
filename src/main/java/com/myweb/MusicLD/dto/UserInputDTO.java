package com.myweb.MusicLD.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.AvatarResponse;
import com.myweb.MusicLD.dto.response.CommentResponse;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInputDTO {
    private BigInteger id;
    private String username;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dateOfBirth;
    private String nickName;
    private Boolean gender;
    private RoleResponse roles;
    private AvatarResponse avatar;
    private RequestFollowStatus statusFollower;
    private List<CommentResponse> musicResponses;
    private long countFollower;
    private boolean status;
}
