package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.request.PaymentRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.MusicResponse;
import com.myweb.MusicLD.dto.response.PaymentResponse;
import com.myweb.MusicLD.response.ResponseObject;
import com.myweb.MusicLD.service.PaymentService;
import com.myweb.MusicLD.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    @GetMapping("/vn-pay")
    public ApiResponse<PaymentResponse> pay(HttpServletRequest request) {
        return ApiResponse.<PaymentResponse>builder().result(paymentService.createVnPayPayment(request)).build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentResponse> payCallbackHandler(HttpServletRequest request, HttpServletResponse response,
                                                           @RequestParam(value = "vnp_ResponseCode") String code,
                                                           @RequestParam(value = "vnp_Amount") String amount,
                                                           @RequestParam(value = "vnp_BankCode") String bankCode,
                                                           @RequestParam(value = "userName") String userName
    ) throws IOException {
        response.sendRedirect("http://localhost:3000/upload");
        PaymentResponse paymentDTO = paymentService.save(PaymentRequest.builder()
                .amount(Long.parseLong(amount))
                .code(code)
                .bankCode(bankCode)
                .userEntity(userService.findByUsername(userName))
                .build());
        return ApiResponse.<PaymentResponse>builder().result(paymentDTO).build();

    }
}
