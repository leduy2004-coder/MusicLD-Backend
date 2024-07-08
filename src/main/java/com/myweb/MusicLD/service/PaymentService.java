package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.PaymentDTO;
import com.myweb.MusicLD.dto.TokenDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    public PaymentDTO createVnPayPayment(HttpServletRequest request);
    PaymentDTO save(PaymentDTO payment);
}
