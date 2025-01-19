package com.myweb.MusicLD.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweb.MusicLD.service.ChatBoxService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatBoxImpl implements ChatBoxService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;
    private final WebClient.Builder webClientBuilder;

    @Override
    public String getAnswer(String question) {
        WebClient webClient = webClientBuilder.build();
        Map<String, Object> request = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{
                        Map.of("text", question),
                })
        });

        // Gửi request và nhận response dưới dạng String
        String response = webClient.post().uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request).retrieve().bodyToMono(String.class).block();

        // Xử lý JSON để lấy phần "text"
        try {
            // Sử dụng Jackson ObjectMapper để parse JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            // Truy cập vào phần "candidates" và lấy "text" từ "content"
            JsonNode candidates = rootNode.path("candidates");
            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode content = candidates.get(0).path("content").path("parts");
                if (content.isArray() && !content.isEmpty()) {
                    return content.get(0).path("text").asText(); // Trả về phần "text"
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }

        return "No answer found";
    }
}
