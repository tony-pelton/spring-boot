package com.dsrts.command.configuration;

import com.dsrts.command.clients.Books;
import com.dsrts.command.clients.Customers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
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

    @Bean
    @LoadBalanced
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
    public HttpServiceProxyFactory httpServiceProxyFactoryIntegration(RestClient.Builder restClientBuilder, JdkClientHttpRequestFactory requestFactory) {

        RestClient restClient = restClientBuilder
                .baseUrl("http://integration")
                .requestFactory(requestFactory)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);

        return HttpServiceProxyFactory
                .builderFor(restClientAdapter)
                .build();
    }

    @Bean
    public Books restClientBooks(@Qualifier("httpServiceProxyFactoryIntegration") HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(Books.class);
    }

    @Bean
    public Customers restClientCustomers(@Qualifier("httpServiceProxyFactoryIntegration") HttpServiceProxyFactory httpServiceProxyFactory) {
        return httpServiceProxyFactory.createClient(Customers.class);
    }
}
