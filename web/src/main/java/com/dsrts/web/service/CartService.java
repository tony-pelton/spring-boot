package com.dsrts.web.service;

import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.entities.CartEntity;
import com.dsrts.web.entities.CartItemEntity;
import com.dsrts.web.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

import com.dsrts.web.repository.CustomerRepository;
import com.dsrts.web.entities.CustomerEntity;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public CartEntity getCurrentCart() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails principal = (UserDetails)authentication.getPrincipal();

        Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findByEmail(principal.getUsername());
        CustomerEntity customer = optionalCustomerEntity.get();

        return customer.getCarts().stream()
                .filter(cartEntity -> CartEntity.CartStatus.OPEN.equals(cartEntity.getStatus()))
                .findFirst()
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setCartItems(new HashSet<>());
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public CartItemEntity addToCart(BookEntity book, int quantity) {

        CartEntity cart = getCurrentCart();

        CartItemEntity cartItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItemEntity newItem = new CartItemEntity();
                    newItem.setCart(cart);
                    newItem.setBook(book);
                    newItem.setQty(0);
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQty(cartItem.getQty() + quantity);
        cartRepository.save(cart);

        return cartItem;
    }
}
