package com.dsrts.web.controller;

import com.dsrts.web.entities.BookEntity;
import com.dsrts.web.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BookListController {

    private final BookRepository bookRepository;

    @GetMapping("/books")
    public String viewBooks(
            @PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable,
            Model model
    ) {
        Page<BookEntity> bookPage = bookRepository.findAll(pageable);

        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", bookPage.getNumber());
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("totalItems", bookPage.getTotalElements());
        model.addAttribute("pageSize", pageable.getPageSize());
        model.addAttribute("sortField", pageable.getSort().iterator().hasNext() ? pageable.getSort().iterator().next().getProperty() : "title");
        model.addAttribute("sortOrder", pageable.getSort().iterator().hasNext() ? pageable.getSort().iterator().next().getDirection().toString().toLowerCase() : "asc");
        model.addAttribute("reverseSortOrder", pageable.getSort().iterator().hasNext() && pageable.getSort().iterator().next().getDirection() == Sort.Direction.ASC ? "desc" : "asc");

        return "/books/list";
    }
}