package com.dsrts.integration.service;

import org.springframework.messaging.Message;

import java.util.Map;

@FunctionalInterface
public interface SummaryStrategy {

    String apply(Message<Map<String,String>> payload);
}
