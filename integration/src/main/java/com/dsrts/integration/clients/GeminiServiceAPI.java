package com.dsrts.integration.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.bind.annotation.RequestHeader;

import com.dsrts.integration.clients.GeminiContentRequest;
import com.dsrts.integration.clients.GeminiContentResponse;

@HttpExchange("/v1beta/models/gemini-2.0-flash:generateContent")
public interface GeminiServiceAPI {
    @PostExchange("?key={apiKey}")
    GeminiContentResponse generateContent(@RequestBody GeminiContentRequest requestBody,
                                          @RequestHeader("Content-Type") String contentType,
                                          @PathVariable("apiKey") String apiKey);
}
