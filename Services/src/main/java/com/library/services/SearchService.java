package com.library.services;

import com.library.dto.BookPaginatedDTO;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    BookPaginatedDTO searchBooks(String query, Pageable pageable);

    BookPaginatedDTO searchBooksForBorrower(String borrowerName, int cardId, String isbn, Pageable pageable);
}
