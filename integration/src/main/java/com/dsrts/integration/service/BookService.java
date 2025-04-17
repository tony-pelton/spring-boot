package com.dsrts.integration.service;

import com.dsrts.integration.clients.BooksServiceAPI;
import com.dsrts.integration.clients.GeminiServiceAPI;
import com.dsrts.integration.clients.GeminiContentRequest;
import com.dsrts.integration.clients.GeminiContentResponse;
import com.dsrts.integration.configuration.ChannelConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BookService {

    private final JdbcChannelMessageStore messageStore;
    private final BooksServiceAPI webBooks;
    private final BooksServiceAPI warehouseBooks;
    private final GeminiServiceAPI geminiServiceAPI;

    private String apiKey;

    @Value("${apikey}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public BookService(JdbcChannelMessageStore messageStore,
                       @Qualifier("restClientWebBooks") BooksServiceAPI webBooks,
                       @Qualifier("restClientWarehouseBooks") BooksServiceAPI warehouseBooks,
                       @Qualifier("geminiServiceAPI") GeminiServiceAPI geminiServiceAPI)
    {
        this.messageStore = messageStore;
        this.webBooks = webBooks;
        this.warehouseBooks = warehouseBooks;
        this.geminiServiceAPI = geminiServiceAPI;
    }

    @Transactional
    @ServiceActivator(inputChannel = ChannelConfiguration.BOOK_GATEWAY_IN_CHANNEL)
    public void handleBookWebMessage(Message<?> message) throws MessagingException {

        log.info("handleBookWebMessage() : {}",message);
        messageStore.addMessageToGroup(ChannelConfiguration.BOOK_WEB_MESSAGE, message);
    }

    @Transactional
    @ServiceActivator(inputChannel = ChannelConfiguration.BOOK_GATEWAY_IN_CHANNEL)
    public void handleBookWarehouseMessage(Message<?> message) throws MessagingException {

        log.info("handleBookWarehouseMessage() : {}",message);
        messageStore.addMessageToGroup(ChannelConfiguration.BOOK_WAREHOUSE_MESSAGE, message);
    }

    @Transactional
    public boolean processBookWebMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.BOOK_WEB_MESSAGE);
        log.info("processBookWebMessage() : {}", message);
        if (StringUtils.hasLength(apiKey) && message != null && message.getPayload() != null) {
            Map<String, String> payload = message.getPayload();
            if (StringUtils.hasLength(apiKey)) {
                String summary = getGeminiSummary(payload);
                payload.put("summary", summary.trim());
            } else {
                payload.put("summary", "If you were using a Gemini API key, you'd see a cool summary here.");
            }
        }
        return processBookMessage(message, webBooks);
    }

    private String getGeminiSummary(Map<String, String> payload) {
        String title = payload.getOrDefault("title", "");
        // Build Gemini API request POJO
        GeminiContentRequest.Part part = new GeminiContentRequest.Part("Short summary for the book \"" + title + "\"");
        GeminiContentRequest.Content content = new GeminiContentRequest.Content(List.of(part));
        GeminiContentRequest geminiRequest = new GeminiContentRequest(List.of(content));
        GeminiContentResponse geminiResponse = geminiServiceAPI.generateContent(geminiRequest, "application/json", apiKey);
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

    @Transactional
    public boolean processBookWarehouseMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.BOOK_WAREHOUSE_MESSAGE);
        log.info("processBookWarehouseMessage() : {}", message);
        return processBookMessage(message,warehouseBooks);
    }

    private boolean processBookMessage(Message<Map<String,String>> message, BooksServiceAPI booksServiceAPI) {
        if (null != message) {

            /*
            assume 4XX errors will never work or be accepted by the target.
             */
            try {
                booksServiceAPI.add(message.getPayload());
            } catch (HttpClientErrorException e) {
                log.error("book status={}", e.getStatusCode());
                if(!e.getStatusCode().is4xxClientError()) {
                    throw e;
                }
            }

            return true;
        } else {

            return false;
        }
    }
}
