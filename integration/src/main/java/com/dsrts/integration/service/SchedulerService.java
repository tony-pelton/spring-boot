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
    public void processBookMessages() {

        List<CompletableFuture<Boolean>> completableFutures = new ArrayList<>();
        {
            int count = messageStore.messageGroupSize(ChannelConfiguration.BOOK_WEB_MESSAGE);
            for (int i = 0; i < count; i++) {
                completableFutures.add(bookService.processBookWebMessage());
            }
        }
        {
            int count = messageStore.messageGroupSize(ChannelConfiguration.BOOK_WAREHOUSE_MESSAGE);
            for (int i = 0; i < count; i++) {
                completableFutures.add(bookService.processBookWarehouseMessage());
            }
        }

        completableFutures.forEach(booleanCompletableFuture -> {
            try {
                booleanCompletableFuture.get();
            } catch (Exception e) {
                log.error("processBookMessages()",e);
            }
        });
    }
}
