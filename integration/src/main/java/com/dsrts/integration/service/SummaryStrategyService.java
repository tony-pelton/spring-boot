package com.dsrts.integration.service;

import com.dsrts.integration.clients.GeminiContentRequest;
import com.dsrts.integration.clients.GeminiContentResponse;
import com.dsrts.integration.clients.GeminiServiceAPI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SummaryStrategyService {

    private final GeminiServiceAPI geminiServiceAPI;

    private String geminiApiKey;

    @Value("${application.geminiApiKey}")
    public void setGeminiApiKey(String geminiApiKey) {
        this.geminiApiKey = geminiApiKey;
    }

    public SummaryStrategy find(Message<Map<String,String>> message) {
        if(StringUtils.hasLength(geminiApiKey)) {

            return this::gemini;
        } else {

            return this::plain;
        }
    }

    private String gemini(Message<Map<String,String>> message) {
        String summary = "";
        if (message != null && message.getPayload() != null) {
            Map<String, String> payload = message.getPayload();
            summary = getGeminiSummary(payload).trim();
        }

        return summary;

    }

    private String getGeminiSummary(Map<String, String> payload) {

        String title = payload.getOrDefault("title", "");
        // Build Gemini API request POJO
        GeminiContentRequest.Part part = new GeminiContentRequest.Part("Short summary for the book \"" + title + "\"");
        GeminiContentRequest.Content content = new GeminiContentRequest.Content(List.of(part));
        GeminiContentRequest geminiRequest = new GeminiContentRequest(List.of(content));
        GeminiContentResponse geminiResponse = geminiServiceAPI.generateContent(geminiRequest, "application/json", geminiApiKey);
        String summary = "";
        if (geminiResponse != null && geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
            GeminiContentResponse.Candidate candidate = geminiResponse.getCandidates().get(0);
            if (candidate != null && candidate.getContent() != null && candidate.getContent().getParts() != null && !candidate.getContent().getParts().isEmpty()) {
                GeminiContentResponse.Part responsePart = candidate.getContent().getParts().get(0);
                if (responsePart != null && responsePart.getText() != null) {
                    summary = responsePart.getText();
                }
            }
        }
        return summary;
    }


    private String plain(Message<Map<String,String>> message) {

        return "If you were using a Gemini API key, you'd see a cool summary here.";
    }
}
