package com.myweb.MusicLD.dto;

import com.myweb.MusicLD.entity.UserEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDTO extends BaseDTO{
        private String code;
        private String message;
        private String paymentUrl;

        private Long amount;
        private String bankCode;
        private UserDTO userEntity;

}
