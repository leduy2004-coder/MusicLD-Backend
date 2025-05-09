package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {
        String code;
        String message;
        String paymentUrl;

        Long amount;
        String bankCode;
        UserResponse userEntity;

}
