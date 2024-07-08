package com.myweb.MusicLD.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    String sendEmail(MultipartFile[] file, String to, String cc, String subject, String body);
}
