package com.dsrts.web.controller;

import com.dsrts.web.dto.CartItemRequest;
import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.entities.CartEntity;
import com.dsrts.web.repository.BookRepository;
import com.dsrts.web.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final BookRepository bookRepository;
    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getOpenCart());
        var groupedCarts = cartService.getCartsGroupedByStatus();
        model.addAttribute("orderedCarts", groupedCarts.getOrDefault(CartEntity.CartStatus.ORDERED, List.of()));
        model.addAttribute("shippedCarts", groupedCarts.getOrDefault(CartEntity.CartStatus.SHIPPED, List.of()));
        return "cart/view";
    }

    @PostMapping("/cart/checkout")
    public String checkout(RedirectAttributes redirectAttributes) {
        try {
            cartService.checkout();
            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to place order: " + e.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute CartItemRequest request, RedirectAttributes redirectAttributes) {
        try {
            if (request.getQuantity() < 1 || request.getQuantity() > 5) {
                redirectAttributes.addFlashAttribute("error", "Quantity must be between 1 and 5.");
            } else {

                // Validate the book exists
                BookEntity book = bookRepository.findById(request.getBookId())
                        .orElse(null);

                if (book == null) {
                    redirectAttributes.addFlashAttribute("error", "Book not found");
                    return "redirect:/books";
                }

                cartService.addToCart(book, request.getQuantity());
                redirectAttributes.addFlashAttribute("success",
                        String.format("Added %d item(s) to cart", request.getQuantity()));
            }

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/books/" + request.getBookId();
    }
}
