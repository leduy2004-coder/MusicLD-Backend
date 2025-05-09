package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailSendController {

    EmailService emailService;

    @PostMapping("/send")
    public Boolean sendEmail(@RequestParam(value = "file", required = false)MultipartFile[] file, String to, String cc, String subject, String body){
        return emailService.sendEmail(file,to,cc,subject,body);
    }
}
