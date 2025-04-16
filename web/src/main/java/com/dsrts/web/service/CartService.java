package com.dsrts.web.service;

import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.entities.CartEntity;
import com.dsrts.web.entities.CartItemEntity;
import com.dsrts.web.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public CartEntity getCurrentCart() {
        // TODO: Replace with actual user's cart once authentication is implemented
        return cartRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> {
                    CartEntity newCart = new CartEntity();
                    newCart.setCartItems(new HashSet<>());
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public CartItemEntity addToCart(BookEntity book, int quantity) {
        if (quantity < 1 || quantity > 99) {
            throw new IllegalArgumentException("Quantity must be between 1 and 99");
        }

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
        return cartRepository.save(cart).getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElseThrow();
    }
}
