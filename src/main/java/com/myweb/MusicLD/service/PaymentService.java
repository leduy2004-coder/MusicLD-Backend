package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.PaymentRequest;
import com.myweb.MusicLD.dto.response.PaymentResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    PaymentResponse createVnPayPayment(HttpServletRequest request);
    PaymentResponse save(PaymentRequest payment);
}
