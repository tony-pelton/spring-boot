package com.dsrts.integration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.jdbc.store.JdbcChannelMessageStore;
import org.springframework.integration.jdbc.store.channel.H2ChannelMessageStoreQueryProvider;

import javax.sql.DataSource;

@Configuration
public class ChannelConfiguration {

    /* the http gateway publish channel */
    public static final String BOOK_GATEWAY_IN_CHANNEL = "bookGatewayInChannel";
    public static final String CUSTOMER_GATEWAY_IN_CHANNEL = "customerGatewayInChannel";

    public static final String BOOK_WEB_MESSAGE = "bookWebMessage";
    public static final String BOOK_WAREHOUSE_MESSAGE = "bookWarehouseMessage";

    public static final String CUSTOMER_WEB_MESSAGE = "customerWebMessage";
    public static final String CUSTOMER_WAREHOUSE_MESSAGE = "customerWarehouseMessage";

    @Bean(BOOK_GATEWAY_IN_CHANNEL)
    public PublishSubscribeChannel bookGatewayInChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean(CUSTOMER_GATEWAY_IN_CHANNEL)
    public PublishSubscribeChannel customerGatewayInChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public JdbcChannelMessageStore channelMessageStore(DataSource dataSource) {
        JdbcChannelMessageStore jdbcChannelMessageStore = new JdbcChannelMessageStore(dataSource);
        jdbcChannelMessageStore.setChannelMessageStoreQueryProvider(new H2ChannelMessageStoreQueryProvider());
        return jdbcChannelMessageStore;
    }

}
