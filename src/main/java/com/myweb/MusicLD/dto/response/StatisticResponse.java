package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class StatisticResponse {
    private int months;
    private String nickName;
    private BigDecimal userId;
    private int totalMusic;
    private int countMusic;
    private double totalAmount;
    private int totalAccount;
    private int totalFollower;
    private AvatarResponse avatar;
}
