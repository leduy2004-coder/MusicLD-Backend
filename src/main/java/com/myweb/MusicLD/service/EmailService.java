package com.myweb.MusicLD.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    Boolean sendEmail(MultipartFile[] file, String to, String cc, String subject, String body);

    String generateOtp();

    void sendOtp(String email, String userId);

    boolean checkOTP(String otp, String email, String userId);


}
