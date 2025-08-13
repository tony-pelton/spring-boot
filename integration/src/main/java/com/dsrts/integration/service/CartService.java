package com.dsrts.integration.service;

import com.dsrts.integration.clients.CartServiceAPI;
import com.dsrts.integration.configuration.RestClientConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CartService {

    private final JdbcChannelMessageStore messageStore;
    private final CartServiceAPI webCarts;
    private final CartServiceAPI warehouseCarts;

    public CartService(JdbcChannelMessageStore messageStore,
                       @Qualifier(RestClientConfiguration.REST_CLIENT_WEB_CARTS) CartServiceAPI cartServiceWebAPI,
                       @Qualifier(RestClientConfiguration.REST_CLIENT_WAREHOUSE_CARTS) CartServiceAPI cartServiceWarehouseAPI) {
        this.messageStore = messageStore;
        this.webCarts = cartServiceWebAPI;
        this.warehouseCarts = cartServiceWarehouseAPI;
    }
}
