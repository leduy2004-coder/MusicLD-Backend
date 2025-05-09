package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatisticResponse {
    int months;
    String nickName;
    BigDecimal userId;
    int totalMusic;
    int countMusic;
    double totalAmount;
    int totalAccount;
    int totalFollower;
    AvatarResponse avatar;
}
