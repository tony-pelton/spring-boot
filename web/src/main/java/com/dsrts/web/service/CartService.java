package com.dsrts.web.service;

import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.entities.CartEntity;
import com.dsrts.web.entities.CartItemEntity;
import com.dsrts.web.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import static com.dsrts.web.service.SecurityUtils.getCurrentUserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

import com.dsrts.web.repository.CustomerRepository;
import com.dsrts.web.entities.CustomerEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public Map<CartEntity.CartStatus, List<CartEntity>> getCartsGroupedByStatus() {
        UserDetails principal = getCurrentUserDetails();
        String email = principal.getUsername();
        List<CartEntity> allCarts = cartRepository.findByCustomerEmail(email);
        return allCarts.stream().collect(Collectors.groupingBy(CartEntity::getStatus));
    }

    @Transactional
    public CartEntity getOpenCart() {
        UserDetails principal = getCurrentUserDetails();
        String email = principal.getUsername();
        List<CartEntity> openCarts = cartRepository.findByCustomerEmailAndStatus(email, CartEntity.CartStatus.OPEN);
        if (!openCarts.isEmpty()) {
            return openCarts.get(0);
        } else {
            Optional<CustomerEntity> optionalCustomerEntity = customerRepository.findByEmail(email);
            CustomerEntity customer = optionalCustomerEntity.get();
            CartEntity newCart = new CartEntity();
            newCart.setCartItems(new HashSet<>());
            newCart.setCustomer(customer);
            return cartRepository.save(newCart);
        }
    }

    @Transactional
    public void checkout() {
        CartEntity cart = getOpenCart();
        cart.setStatus(CartEntity.CartStatus.ORDERED);
        cartRepository.save(cart);
    }


    @Transactional
    public CartItemEntity addToCart(BookEntity book, int quantity) {

        CartEntity cart = getOpenCart();

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
