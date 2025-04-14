package com.dsrts.integration.service;

import com.dsrts.integration.clients.BooksServiceAPI;
import com.dsrts.integration.configuration.ChannelConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class BookService {

    private final JdbcChannelMessageStore messageStore;
    private final BooksServiceAPI webBooks;
    private final BooksServiceAPI warehouseBooks;

    public BookService(JdbcChannelMessageStore messageStore,
                       @Qualifier("restClientWebBooks") BooksServiceAPI webBooks,
                       @Qualifier("restClientWarehouseBooks") BooksServiceAPI warehouseBooks)
    {
        this.messageStore = messageStore;
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

    @Async
    @Transactional
    public CompletableFuture<Boolean> processBookWebMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.BOOK_WEB_MESSAGE);
        log.info("processBookWebMessage() : {}", message);
        return processBookMessage(message, webBooks);
    }

    @Async
    @Transactional
    public CompletableFuture<Boolean> processBookWarehouseMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.BOOK_WAREHOUSE_MESSAGE);
        log.info("processBookWarehouseMessage() : {}", message);
        return processBookMessage(message,warehouseBooks);
    }

    private CompletableFuture<Boolean> processBookMessage(Message<Map<String,String>> message, BooksServiceAPI booksServiceAPI) {
        if (null != message) {

            /*
            assume 4XX errors will never work be accepted by the target.
             */
            try {
                booksServiceAPI.add(message.getPayload());
            } catch (HttpClientErrorException e) {
                log.error("book status={}", e.getStatusCode());
                if(!e.getStatusCode().is4xxClientError()) {
                    throw e;
                }
            }

            return CompletableFuture.completedFuture(Boolean.TRUE);
        } else {

            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }
}
