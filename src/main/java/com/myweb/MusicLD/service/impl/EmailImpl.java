package com.myweb.MusicLD.service.impl;


import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.service.EmailService;
import com.myweb.MusicLD.service.redis.OtpRedisService;
import com.myweb.MusicLD.service.redis.VerifyRedisService;
import com.myweb.MusicLD.utility.GetInfo;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final OtpRedisService otpRedisService;
    private final VerifyRedisService verifyRedisService;
    private final JavaMailSender javaMailSender;

    @Override
    public Boolean sendEmail(MultipartFile[] file, String to, String cc, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            if (cc != null && !cc.isEmpty()) {
                mimeMessageHelper.setCc(cc);
            }
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            if (file != null) {
                for (MultipartFile multipartFile : file) {
                    if (multipartFile != null && !multipartFile.isEmpty()) {
                        mimeMessageHelper.addAttachment(
                                Objects.requireNonNull(multipartFile.getOriginalFilename()),
                                new ByteArrayResource(multipartFile.getBytes())
                        );
                    }
                }
            }

            javaMailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String generateOtp() {
        Random random = new Random();
        int randomInt = random.nextInt(999999);
        StringBuilder output = new StringBuilder(Integer.toString(randomInt));

        while (output.length() < 6) {
            output.insert(0, "0");
        }

        return output.toString();
    }

    @Override
    public void sendOtp(String email, String type) {
        String otp = generateOtp();
        String subject = "Mã xác thực OTP của bạn";
        String body = String.format(
                """
                        <p>Xin chào,</p>
                        <p>Đây là mã xác thực (OTP) của bạn:</p>
                        <h2 style="color:blue;">%s</h2>
                        <p>Vui lòng nhập mã này trong vòng 5 phút để hoàn tất xác thực.</p>
                        <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>
                        <br>
                        <p>Trân trọng,</p>
                        <p>Đội ngũ hỗ trợ</p>
                        """,
                otp
        );

        sendEmail(null, email, null, subject, body);
        // Kiểm tra và xóa OTP cũ nếu tồn tại
        if(type.equals("CHANGE-PASSWORD")){
            String userName = GetInfo.getLoggedInUserName();
            String existingOtp = verifyRedisService.getVerify(userName);
            if (existingOtp != null) {
                verifyRedisService.clearByVerify(userName);
            }
            verifyRedisService.saveVerify(userName, otp);
        }else {
            String existingOtp = otpRedisService.getOTP(email);
            if (existingOtp != null) {
                otpRedisService.clearByEmail(email);
            }
            otpRedisService.saveOTP(email, otp);
        }

    }

    @Override
    public boolean checkOTP(String otp, String email, String type) {
        if(type.equals("CHANGE-PASSWORD")){
            String userName = GetInfo.getLoggedInUserName();
            String savedOtp = verifyRedisService.getVerify(userName);

            if (savedOtp == null) {
                throw new AppException(ErrorCode.OTP_EXPIRED);
            }
            if(savedOtp.equals(otp)){
                verifyRedisService.clearByVerify(email);
                return true;
            }
        }else {
            String savedOtp = otpRedisService.getOTP(email);

            if (savedOtp == null) {
                throw new AppException(ErrorCode.OTP_EXPIRED);
            }
            if(savedOtp.equals(otp)){
                otpRedisService.clearByEmail(email);
                return true;
            }
        }

        return false;
    }

}
