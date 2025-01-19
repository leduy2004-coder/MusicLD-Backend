package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.service.ChatBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat-box")
@RequiredArgsConstructor
public class ChatBoxController {
    private final ChatBoxService chatBoxService;


    @PostMapping("/ask")
    public ApiResponse<String> askQuestion(@RequestBody Map<String, String> payload) throws IOException {
        String question = payload.get("question");
        String answer = chatBoxService.getAnswer(question);
        return ApiResponse.<String>builder().result(answer).build();
    }

}
