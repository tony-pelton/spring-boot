package com.dsrts.integration.service;

import com.dsrts.integration.clients.BooksServiceAPI;
import com.dsrts.integration.clients.GeminiServiceAPI;
import com.dsrts.integration.clients.GeminiContentRequest;
import com.dsrts.integration.clients.GeminiContentResponse;
import com.dsrts.integration.configuration.ChannelConfiguration;
import com.dsrts.integration.configuration.RestClientConfiguration;
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
    private final SummaryStrategyService summaryStrategyService;
    private final BooksServiceAPI webBooks;
    private final BooksServiceAPI warehouseBooks;

    public BookService(JdbcChannelMessageStore messageStore,
                       SummaryStrategyService summaryStrategyService,
                       @Qualifier(RestClientConfiguration.REST_CLIENT_WEB_BOOKS) BooksServiceAPI webBooks,
                       @Qualifier(RestClientConfiguration.REST_CLIENT_WAREHOUSE_BOOKS) BooksServiceAPI warehouseBooks)
    {
        this.messageStore = messageStore;
        this.summaryStrategyService = summaryStrategyService;
        this.webBooks = webBooks;
        this.warehouseBooks = warehouseBooks;
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
        if (message != null && message.getPayload() != null) {
            Map<String, String> payload = message.getPayload();
            payload.put("summary", summaryStrategyService.find(message).apply(message));
        }
        return processBookMessage(message, webBooks);
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
