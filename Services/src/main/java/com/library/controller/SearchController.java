package com.library.controller;

import com.library.dto.BookPaginatedDTO;
import com.library.services.SearchService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search/books")
    public ResponseEntity<BookPaginatedDTO> search(@RequestParam(name = "q") String query, @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(searchService.searchBooks(query, pageable));
    }

    @GetMapping(value = "/search/borrower")
    public ResponseEntity<?> searchToCheckIn(String name, int cardId, String isbn, @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(searchService.searchBooksForBorrower(name, cardId, isbn, pageable));
    }
}
