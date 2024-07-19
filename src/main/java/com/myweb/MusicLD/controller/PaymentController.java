package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.request.PaymentRequest;
import com.myweb.MusicLD.dto.response.PaymentResponse;
import com.myweb.MusicLD.response.ResponseObject;
import com.myweb.MusicLD.service.PaymentService;
import com.myweb.MusicLD.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    @GetMapping("/vn-pay")
    public ResponseObject<PaymentResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentResponse> payCallbackHandler(HttpServletRequest request,
                                                         @RequestParam(value = "vnp_ResponseCode") String code,
                                                         @RequestParam(value = "vnp_Amount") String amount,
                                                         @RequestParam(value = "vnp_BankCode") String bankCode,
                                                         @RequestParam(value = "userName") String userName
    ) {
        if (code.equals("00")) {
            PaymentResponse paymentDTO = paymentService.save(PaymentRequest.builder()
                    .amount(Long.parseLong(amount))
                    .code(code)
                    .bankCode(bankCode)
                    .userEntity(userService.findByUsername(userName))
                    .build());
            return new ResponseObject<>(HttpStatus.OK, "Success", new PaymentResponse("00", "Success", "",Long.parseLong(amount),bankCode,null));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }
}
