package com.dsrts.integration.service;

import com.dsrts.integration.clients.CartServiceAPI;
import com.dsrts.integration.configuration.ChannelConfiguration;
import com.dsrts.integration.configuration.RestClientConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    private final JdbcChannelMessageStore messageStore;
    private final BookService bookService;
    private final CustomerService customerService;
    private final CartServiceAPI cartWarehouseServiceAPI;
    private final CartServiceAPI cartWebServiceAPI;

    public SchedulerService(JdbcChannelMessageStore messageStore,
                            BookService bookService,
                            CustomerService customerService,
                            @Qualifier(RestClientConfiguration.REST_CLIENT_WAREHOUSE_CARTS) CartServiceAPI cartWarehouseServiceAPI,
                            @Qualifier(RestClientConfiguration.REST_CLIENT_WEB_CARTS) CartServiceAPI cartWebServiceAPI) {
        this.messageStore = messageStore;
        this.bookService = bookService;
        this.customerService = customerService;
        this.cartWarehouseServiceAPI = cartWarehouseServiceAPI;
        this.cartWebServiceAPI = cartWebServiceAPI;
    }

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

    public void processCartWebMessages() {}

    public void processCartWarehouseMessages() {}
}
