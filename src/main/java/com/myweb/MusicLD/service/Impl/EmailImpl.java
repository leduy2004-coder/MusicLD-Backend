package com.myweb.MusicLD.service.Impl;


import com.myweb.MusicLD.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;


    private final JavaMailSender javaMailSender;

    @Override
    public String sendEmail(MultipartFile[] file, String to, String cc, String subject, String body) {
        try {
            if (file == null) {
                throw new IllegalArgumentException("File array cannot be null");
            }

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setCc(cc);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            for (MultipartFile multipartFile : file) {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    mimeMessageHelper.addAttachment(
                            Objects.requireNonNull(multipartFile.getOriginalFilename()),
                            new ByteArrayResource(multipartFile.getBytes())
                    );
                }
            }

            javaMailSender.send(mimeMessage);
            return "Mail sent";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
