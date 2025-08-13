package com.dsrts.integration.clients;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@HttpExchange("/api/cart")
public interface CartServiceAPI {

    public enum CartStatus {
        OPEN,
        ORDERED,
        SHIPPED
    }

    @GetExchange
    List<Map<String,String>> getAllCarts();
    
    @GetExchange("/{id}")
    Map<String,String> getCartById(Long id);
    
//    @GetExchange
//    List<Cart> findByCustomerEmail(@RequestParam("email") String email);
    
//    @GetExchange
//    List<Cart> findByCustomerEmailAndStatus(@RequestParam("email") String email, @RequestParam("status") Cart.CartStatus status);
    
    @GetExchange
    List<Map<String,String>> findByStatus(@RequestParam("status") CartStatus status);
}
