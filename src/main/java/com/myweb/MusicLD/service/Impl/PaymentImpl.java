package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.config.Payment.VNPAYConfig;
import com.myweb.MusicLD.dto.CustomOAuth2User;
import com.myweb.MusicLD.dto.CustomUserDetails;
import com.myweb.MusicLD.dto.PaymentDTO;
import com.myweb.MusicLD.entity.PaymentEntity;
import com.myweb.MusicLD.repository.PaymentRepository;
import com.myweb.MusicLD.service.PaymentService;
import com.myweb.MusicLD.utility.GetInfo;
import com.myweb.MusicLD.utility.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentImpl implements PaymentService {

    @Value("${payment.vnPay.returnUrl}")
    private String vnp_ReturnUrl;

    private final VNPAYConfig vnPayConfig;
    private final PaymentRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public PaymentDTO createVnPayPayment(HttpServletRequest request) {
        String userName = Objects.requireNonNull(GetInfo.getLoggedInUserInfo()).getUsername();
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl + "?userName=" + userName);
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return PaymentDTO.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    @Override
    public PaymentDTO save(PaymentDTO payment) {
        return modelMapper.map(repository.save(modelMapper.map(payment, PaymentEntity.class)), PaymentDTO.class);
    }


}
