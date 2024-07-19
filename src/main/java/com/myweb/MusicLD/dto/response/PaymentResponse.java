package com.myweb.MusicLD.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
        private String code;
        private String message;
        private String paymentUrl;

        private Long amount;
        private String bankCode;
        private UserResponse userEntity;

}
