package com.dsrts.command.clients;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange("/api/customer")
public interface Customers {
    @PostExchange
    void add(@RequestBody Map<String,String> customer);
}
