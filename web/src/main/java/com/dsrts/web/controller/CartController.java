package com.dsrts.web.controller;

import com.dsrts.web.dto.CartItemRequest;
import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.repository.BookRepository;
import com.dsrts.web.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final BookRepository bookRepository;
    private final CartService cartService;

    @GetMapping("/cart")
    public String viewCart(Model model) {
        model.addAttribute("cart", cartService.getCurrentCart());
        return "cart/view";
    }

    @PostMapping("/cart/add")
    public String addToCart(@ModelAttribute CartItemRequest request, RedirectAttributes redirectAttributes) {
        try {
            // Validate the book exists
            BookEntity book = bookRepository.findById(request.getBookId())
                    .orElseGet(() -> {
                        redirectAttributes.addFlashAttribute("error", "Book not found");
                        return null;
                    });

            if (book == null) {
                return "redirect:/books";
            }

            cartService.addToCart(book, request.getQuantity());
            redirectAttributes.addFlashAttribute("success", 
                String.format("Added %d item(s) to cart", request.getQuantity()));
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/books/" + request.getBookId();
    }
}
