package com.myweb.MusicLD.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.myweb.MusicLD.dto.response.UserResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequest {
        private String code;
        private String message;
        private String paymentUrl;

        private Long amount;
        private String bankCode;
        private UserResponse userEntity;

}
