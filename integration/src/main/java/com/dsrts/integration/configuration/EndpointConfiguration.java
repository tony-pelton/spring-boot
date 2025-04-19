package com.dsrts.integration.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.integration.http.inbound.RequestMapping;

import java.util.HashMap;

@Configuration
public class EndpointConfiguration {
    @Bean
    public HttpRequestHandlingMessagingGateway httpInboundBookGateway(@Qualifier(ChannelConfiguration.BOOK_GATEWAY_IN_CHANNEL) PublishSubscribeChannel requestChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(false); // One-way
        RequestMapping mapping = new RequestMapping();
        mapping.setPathPatterns("/api/book");
        mapping.setMethods(HttpMethod.POST);
        gateway.setRequestMapping(mapping);
        gateway.setRequestPayloadType(ResolvableType.forInstance(new HashMap<String,String>()));
        gateway.setRequestChannel(requestChannel);
        SpelExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("200");
        gateway.setStatusCodeExpression(expression);
        return gateway;
    }

    @Bean
    public HttpRequestHandlingMessagingGateway httpInboundCustomerGateway(@Qualifier(ChannelConfiguration.CUSTOMER_GATEWAY_IN_CHANNEL) PublishSubscribeChannel requestChannel) {
        HttpRequestHandlingMessagingGateway gateway = new HttpRequestHandlingMessagingGateway(false); // One-way
        RequestMapping mapping = new RequestMapping();
        mapping.setPathPatterns("/api/customer");
        mapping.setMethods(HttpMethod.POST);
        gateway.setRequestMapping(mapping);
        gateway.setRequestPayloadType(ResolvableType.forInstance(new HashMap<String,String>()));
        gateway.setRequestChannel(requestChannel);
        SpelExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("200");
        gateway.setStatusCodeExpression(expression);
        return gateway;
    }
}
