package com.dsrts.integration.service;

import com.dsrts.integration.clients.CustomersServiceAPI;
import com.dsrts.integration.configuration.ChannelConfiguration;
import com.dsrts.integration.configuration.RestClientConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Slf4j
@Service
public class CustomerService {

    private final JdbcChannelMessageStore messageStore;
    private final CustomersServiceAPI webCustomers;
    private final CustomersServiceAPI warehouseCustomers;

    public CustomerService(JdbcChannelMessageStore messageStore,
                           @Qualifier(RestClientConfiguration.REST_CLIENT_WEB_CUSTOMERS) CustomersServiceAPI webCustomers,
                           @Qualifier(RestClientConfiguration.REST_CLIENT_WAREHOUSE_CUSTOMERS) CustomersServiceAPI warehouseCustomers)
    {
        this.messageStore = messageStore;
        this.webCustomers = webCustomers;
        this.warehouseCustomers = warehouseCustomers;
    }

    @Transactional
    @ServiceActivator(inputChannel = ChannelConfiguration.CUSTOMER_GATEWAY_IN_CHANNEL)
    public void handleCustomerWebMessage(Message<?> message) throws MessagingException {

        log.info("handleCustomerWebMessage() : {}",message);
        messageStore.addMessageToGroup(ChannelConfiguration.CUSTOMER_WEB_MESSAGE, message);
    }

    @Transactional
    @ServiceActivator(inputChannel = ChannelConfiguration.CUSTOMER_GATEWAY_IN_CHANNEL)
    public void handleCustomerWarehouseMessage(Message<?> message) throws MessagingException {

        log.info("handleCustomerWarehouseMessage() : {}",message);
        messageStore.addMessageToGroup(ChannelConfiguration.CUSTOMER_WAREHOUSE_MESSAGE, message);
    }

    @Transactional
    public boolean processCustomerWebMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.CUSTOMER_WEB_MESSAGE);
        log.info("processCustomerWebMessage() : {}", message);
        return processCustomerMessage(message,webCustomers);
    }

    @Transactional
    public boolean processCustomerWarehouseMessage() {
        Message<Map<String,String>> message = (Message<Map<String,String>>)messageStore.pollMessageFromGroup(ChannelConfiguration.CUSTOMER_WAREHOUSE_MESSAGE);
        log.info("processCustomerWarehouseMessage() : {}", message);
        return processCustomerMessage(message,warehouseCustomers);
    }

    private boolean processCustomerMessage(Message<Map<String,String>> message, CustomersServiceAPI customersServiceAPI) {
        if (null != message) {

            /*
            assume 4XX errors will never work or be accepted by the target.
             */
            try {
                customersServiceAPI.add(message.getPayload());
            } catch (HttpClientErrorException e) {
                log.error("customer status={}", e.getStatusCode());
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
