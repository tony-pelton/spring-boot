package com.dsrts.integration.configuration;

import com.dsrts.integration.clients.BooksServiceAPI;

import com.dsrts.integration.clients.GeminiServiceAPI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    @LoadBalanced
    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient
                .builder();
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    @Bean
    public JdkClientHttpRequestFactory jdkClientHttpRequestFactory(HttpClient httpClient) {

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        return requestFactory;
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactoryWeb(RestClient.Builder restClientBuilder, JdkClientHttpRequestFactory requestFactory) {

        RestClient restClient = restClientBuilder
                .baseUrl("http://web")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);

        return HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactoryWarehouse(RestClient.Builder restClientBuilder, JdkClientHttpRequestFactory requestFactory) {
        RestClient restClient = restClientBuilder
                .baseUrl("http://warehouse")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);

        return HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();
    }

    @Bean
public HttpServiceProxyFactory httpServiceProxyFactoryGemini(JdkClientHttpRequestFactory requestFactory) {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .requestFactory(requestFactory)
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        return HttpServiceProxyFactory.builderFor(restClientAdapter).build();
    }

    @Bean
    public GeminiServiceAPI geminiServiceAPI(@Qualifier("httpServiceProxyFactoryGemini") HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(GeminiServiceAPI.class);
    }

    @Bean
    public BooksServiceAPI restClientWarehouseBooks(@Qualifier("httpServiceProxyFactoryWarehouse") HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(BooksServiceAPI.class);
    }

    @Bean
    public BooksServiceAPI restClientWebBooks(@Qualifier("httpServiceProxyFactoryWeb") HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(BooksServiceAPI.class);
    }

}
