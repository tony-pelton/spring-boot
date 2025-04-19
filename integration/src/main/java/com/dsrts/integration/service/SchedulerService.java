package com.dsrts.integration.service;

import com.dsrts.integration.configuration.ChannelConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerService {

    private final JdbcChannelMessageStore messageStore;
    private final BookService bookService;
    private final CustomerService customerService;

    private void processBookWebMessages() {

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

    private void processCustomerWebMessages() {

        int count = messageStore.messageGroupSize(ChannelConfiguration.CUSTOMER_WEB_MESSAGE);
        if(0 < count) {
            log.info("processCustomerWebMessages() count={}", count);
        }
        for (int i = 0; i < count; i++) {
            if (!customerService.processCustomerWebMessage()) {
                return;
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processWebMessages() {

        try {

            processBookWebMessages();
        } catch (Exception e) {

            log.error("processWebMessages()",e);
        }

        try {
            processCustomerWebMessages();

        } catch (Exception e) {

            log.error("processWebMessages()",e);
        }

    }

    private void processBookWarehouseMessages() {

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

    private void processCustomerWarehouseMessages() {

        int count = messageStore.messageGroupSize(ChannelConfiguration.CUSTOMER_WAREHOUSE_MESSAGE);
        if(0 < count) {
            log.info("processCustomerWarehouseMessages() count={}", count);
        }
        for (int i = 0; i < count; i++) {
            if (!customerService.processCustomerWarehouseMessage()) {
                return;
            }
        }
    }

    @Scheduled(fixedDelay = 5000)
    public void processWarehouseMessages() {

        try {

            processBookWarehouseMessages();
        } catch (Exception e) {

            log.error("processWarehouseMessages()",e);
        }

        try {

            processCustomerWarehouseMessages();
        } catch (Exception e) {

            log.error("processWarehouseMessages()",e);
        }

    }
}
