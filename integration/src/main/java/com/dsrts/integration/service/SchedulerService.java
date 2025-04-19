package com.dsrts.integration.service;

import com.dsrts.integration.configuration.ChannelConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerService {

    private final JdbcChannelMessageStore messageStore;
    private final BookService bookService;

    @Scheduled(fixedDelay = 5000)
    public void processBookWebMessages() {

        int count = messageStore.messageGroupSize(ChannelConfiguration.BOOK_WEB_MESSAGE);
        if(0 < count) {
            log.info("processBookWebMessages() count={}", count);
        }
        for (int i = 0; i < count; i++) {
            if (!bookService.processBookWebMessage()) {
                return;
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processBookWarehouseMessages() {

        int count = messageStore.messageGroupSize(ChannelConfiguration.BOOK_WAREHOUSE_MESSAGE);
        if(0 < count) {
            log.info("processBookWarehouseMessages() count={}", count);
        }
        for (int i = 0; i < count; i++) {
            if (!bookService.processBookWarehouseMessage()) {
                return;
            }
        }
    }
}
