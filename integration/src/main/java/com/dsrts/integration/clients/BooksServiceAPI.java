package com.dsrts.integration.clients;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange("/api/book")
public interface BooksServiceAPI {
    @PostExchange
    void add(@RequestBody Map<String,String> bookData);
}
