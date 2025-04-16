package com.dsrts.web.controller;

import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class BookDetailController {

    private final BookRepository bookRepository;

    @GetMapping("/books/{id}")
    public String viewBookDetails(@PathVariable("id") Long id, Model model) {
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
        
        model.addAttribute("book", book);
        return "/books/detail";
    }
}
