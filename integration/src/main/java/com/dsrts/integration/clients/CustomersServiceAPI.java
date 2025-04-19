package com.dsrts.integration.clients;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange("/api/customer")
public interface CustomersServiceAPI {
    @PostExchange
    void add(@RequestBody Map<String,String> bookData);
}
